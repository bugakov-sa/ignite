package presentation.ignite.billing.api.dto.mapper;

import presentation.ignite.billing.api.dto.BillRequest;
import presentation.ignite.billing.entity.ContractProto;
import presentation.ignite.billing.entity.Message;

import java.util.List;
import java.util.stream.Collectors;

public class BillRequestMapper {

    public static Message map(BillRequest request) {
        return new Message(
                request.getMessageId(),
                request.getLogin(),
                request.getSource(),
                request.getOperatorGroup(),
                ContractProto.TrafficType.TRANSACT
        );
    }

    public static List<Message> map(List<BillRequest> request) {
        return request.stream()
                .map(BillRequestMapper::map)
                .collect(Collectors.toList());
    }
}
