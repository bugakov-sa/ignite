package presentation.ignite.billing.task;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.ignite.billing.entity.*;
import presentation.ignite.billing.entity.ContractProto.Contract;
import presentation.ignite.billing.entity.ContractProto.Position;

import static presentation.ignite.billing.entity.BillingResult.*;
import static presentation.ignite.billing.entity.ClusterObjectNames.MONEY_COUNTERS_CACHE;
import static presentation.ignite.billing.entity.ClusterObjectNames.SMS_CONTRACTS_CACHE;
import static presentation.ignite.billing.entity.ClusterObjectNames.SMS_COUNTERS_CACHE;
import static presentation.ignite.billing.util.ContractUtil.*;

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
            System.err.println(e);
            log.error("Ошибка биллинга сообщения " + message, e);
            return new BilledMessage(message.getId(), SYSTEM_ERROR);
        }
    }

    private BilledMessage billInternal(Message message) throws Exception {
        Contract contract = findContract(message.getLogin());
        if (contract == null) {
            return new BilledMessage(message.getId(), CONTRACT_NOT_FOUND);
        }
        Position position = findMessagePosition(contract, message);
        if (position == null) {
            return new BilledMessage(message.getId(), POSITION_NOT_FOUND);
        }
        if (position.getUnionId() == 0) {
            billSimplePosition(position, message);
        } else {
            billUnionPosition(contract, position, message);
        }
        return new BilledMessage(message.getId(), MESSAGE_BILLED);
    }

    private void billSimplePosition(Position position, Message message) {
        String login = message.getLogin();
        long messagesCount = incrementAndGetMessageCounter(position.getId(), login);

        long currStepPrice = getStepPrice(position, messagesCount);
        long balanceChange = -currStepPrice;

        if (position.getRecalcRule() == ContractProto.RecalcRule.STEP) {
            long currStepCount = getStepCount(position, messagesCount);
            if (messagesCount == currStepCount + 1 && currStepCount > 0) {
                long prevStepPrice = getPrevStepPrice(position, messagesCount);
                balanceChange += (messagesCount - 1) * (prevStepPrice - currStepPrice);
            }
        }

        changeLoginBalance(login, balanceChange);
    }

    private void billUnionPosition(Contract contract, Position messagePosition, Message message) {

        String login = message.getLogin();
        long unionId = messagePosition.getUnionId();

        long messagesCountByOnePosition = incrementAndGetMessageCounter(messagePosition.getId(), login);
        long messagesCountByUnion = incrementAndGetMessageCounter(unionId, login);

        long balanceChange = -getStepPrice(messagePosition, messagesCountByUnion);

        for (Position position : getUnionPositions(contract, unionId)) {
            if (position.getRecalcRule() == ContractProto.RecalcRule.STEP) {
                long currStepCount = getStepCount(position, messagesCountByUnion);
                if (messagesCountByUnion == currStepCount + 1 && currStepCount > 0) {
                    long currStepPrice = getStepPrice(position, messagesCountByUnion);
                    long prevStepPrice = getPrevStepPrice(position, messagesCountByUnion);
                    balanceChange += (messagesCountByOnePosition - 1) * (prevStepPrice - currStepPrice);
                }
            }
        }

        changeLoginBalance(login, balanceChange);
    }

    private Contract findContract(String login) throws InvalidProtocolBufferException {
        IgniteCache<String, byte[]> contracts = ignite.cache(SMS_CONTRACTS_CACHE);
        byte[] contractData = contracts.get(login);
        if (contractData == null) {
            return null;
        }
        return Contract.parseFrom(contractData);
    }

    private long incrementAndGetMessageCounter(long counterId, String login) {
        return ignite.<BinaryObject, Long>cache(SMS_COUNTERS_CACHE).invoke(
                ignite.binary().builder("java.lang.String")
                        .setField("id", counterId)
                        .setField("login", login)
                        .build(),
                (entry, arguments) -> {
                    entry.setValue(entry.exists() ? (entry.getValue() + 1) : 1);
                    return entry.getValue();
                }
        );
    }

    private void changeLoginBalance(String login, long balanceChange) {
        ignite.<String, Long>cache(MONEY_COUNTERS_CACHE).invoke(
                login,
                (entry, arguments) -> {
                    entry.setValue(entry.exists()
                            ? (entry.getValue() + balanceChange)
                            : balanceChange
                    );
                    return null;
                });
    }
}
