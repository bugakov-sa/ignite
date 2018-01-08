package presentation.ignite.billing.util;

import presentation.ignite.billing.entity.ContractProto;
import presentation.ignite.billing.entity.Message;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ContractUtil {
    public static ContractProto.Position findMessagePosition(ContractProto.Contract contract, Message message) {
        return contract.getContractDataV1().getPositionsList()
                .stream()
                .filter(p -> isSamePosition(p, message))
                .findFirst()
                .orElse(null);
    }

    public static boolean isSamePosition(ContractProto.Position position, Message message) {
        return position.getSource().equals(message.getSource())
                && position.getOperator().equals(message.getOperator())
                && position.getTrafficType().equals(message.getTrafficType());
    }

    public static long getStepPrice(ContractProto.Position position, long messagesCount) {
        long currStepCount = getStepCount(position, messagesCount);
        return position.getStepsOrThrow(currStepCount);
    }

    public static long getPrevStepPrice(ContractProto.Position position, long messagesCount) {
        long currStepCount = getStepCount(position, messagesCount);
        return new TreeMap<>(position.getStepsMap()).lowerEntry(currStepCount).getValue();
    }

    public static long getStepCount(ContractProto.Position position, long messagesCount) {
        List<Long> steps = new TreeMap<>(position.getStepsMap())
                .keySet()
                .stream()
                .filter(step -> messagesCount >= step + 1)
                .collect(Collectors.toList());
        return steps.get(steps.size() - 1);
    }

    public static List<ContractProto.Position> getUnionPositions(ContractProto.Contract contract, long unionId) {
        return contract.getContractDataV1()
                .getPositionsList()
                .stream()
                .filter(position -> position.getUnionId() == unionId)
                .collect(Collectors.toList());
    }


}
