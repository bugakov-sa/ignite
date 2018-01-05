package presentation.ignite.billing.task;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.ignite.billing.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static presentation.ignite.billing.entity.BillingResult.*;

public class BillSingleMessageTask {

    private static final Logger log = LoggerFactory.getLogger(BillSingleMessageTask.class);

    private final Ignite ignite;

    public BillSingleMessageTask(Ignite ignite) {
        this.ignite = ignite;
    }

    public BilledMessage bill(Message message) {
        try {
            return billInternal(message);
        } catch (Throwable e) {
            log.error("Ошибка биллинга сообщения " + message, e);
            return new BilledMessage(message.getId(), SYSTEM_ERROR);
        }
    }

    private BilledMessage billInternal(Message message) throws Exception {
        ContractProto.Contract contract = findContract(message.getLogin());
        if (contract == null) {
            return new BilledMessage(message.getId(), CONTRACT_NOT_FOUND);
        }
        ContractProto.Position position = findMessagePosition(contract, message);
        if (position == null) {
            return new BilledMessage(message.getId(), POSITION_NOT_FOUND);
        }
        long counterId = Optional.ofNullable(position.getUnionId()).orElse(position.getId());
        long currMessagesCount = incrementAndGetMessageCounter(counterId, message.getLogin());
        long balanceChange = calculateBalanceChange(position, currMessagesCount);
        changeLoginBalance(message.getLogin(), balanceChange);
        return new BilledMessage(message.getId(), MESSAGE_BILLED);
    }

    private ContractProto.Contract findContract(String login) throws InvalidProtocolBufferException {
        IgniteCache<String, byte[]> contracts = ignite.cache(CacheNames.SMS_CONTRACTS);
        byte[] contractData = contracts.get(login);
        if (contractData == null) {
            return null;
        }
        return ContractProto.Contract.parseFrom(contractData);
    }

    private ContractProto.Position findMessagePosition(ContractProto.Contract contract, Message message) {
        return contract.getContractDataV1().getPositionsList()
                .stream()
                .filter(p -> isSamePosition(p, message))
                .findFirst()
                .orElse(null);
    }

    private boolean isSamePosition(ContractProto.Position position, Message message) {
        return position.getSource().equals(message.getSource())
                && position.getOperator().equals(message.getOperator())
                && position.getTrafficType().equals(message.getTrafficType());
    }

    private long incrementAndGetMessageCounter(long counterId, String login) {
        IgniteCache<MessageCounterKey, Long> messageCounters = ignite.cache(CacheNames.SMS_COUNTERS);
        return messageCounters.invoke(
                new MessageCounterKey(login, counterId),
                (entry, arguments) -> {
                    entry.setValue(entry.exists() ? (entry.getValue() + 1) : 1);
                    return entry.getValue();
                }
        );
    }

    private long getCurrStepCount(ContractProto.Position position, long currMessagesCount) {
        List<Long> steps = new TreeMap<>(position.getStepsMap())
                .keySet()
                .stream()
                .filter(step -> currMessagesCount >= step + 1)
                .collect(Collectors.toList());
        return steps.get(steps.size() - 1);
    }

    private long getPrevStepPrice(ContractProto.Position position, long currStepCount) {
        return new TreeMap<>(position.getStepsMap()).lowerEntry(currStepCount).getValue();
    }

    private long calculateBalanceChange(ContractProto.Position position, long currMessagesCount) {
        long currStepCount = getCurrStepCount(position, currMessagesCount);
        long currStepPrice = position.getStepsOrThrow(currStepCount);
        long balanceChange = -currStepPrice;
        if (position.getRecalcRule() == ContractProto.RecalcRule.STEP) {
            if (currMessagesCount == currStepCount + 1 && currStepCount > 0) {
                long prevStepPrice = getPrevStepPrice(position, currStepCount);
                balanceChange += currStepCount * (prevStepPrice - currStepPrice);
            }
        }
        return balanceChange;
    }

    private void changeLoginBalance(String login, long balanceChange) {
        IgniteCache<String, Long> moneyCounters = ignite.cache(CacheNames.MONEY_COUNTERS);
        moneyCounters.invoke(login, (entry, arguments) -> {
            entry.setValue(entry.exists() ? (entry.getValue() + balanceChange) : balanceChange);
            return null;
        });
    }
}
