package presentation.ignite.billing.api.dto.mapper;

import presentation.ignite.billing.api.dto.BillResponse;
import presentation.ignite.billing.entity.BilledMessage;

import java.util.List;
import java.util.stream.Collectors;

public class BillResponseMapper {
    public static BillResponse map(BilledMessage message) {
        return new BillResponse(
                message.getMessageId(),
                message.getBillingResult().ordinal()
        );
    }

    public static List<BillResponse> map(List<BilledMessage> messages) {
        return messages.stream()
                .map(BillResponseMapper::map)
                .collect(Collectors.toList());
    }
}
