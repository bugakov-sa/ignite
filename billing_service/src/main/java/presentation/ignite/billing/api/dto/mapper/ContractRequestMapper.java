package presentation.ignite.billing.api.dto.mapper;

import presentation.ignite.billing.api.dto.ContractRequest;
import presentation.ignite.billing.entity.ContractProto;

import java.util.stream.Collectors;

import static presentation.ignite.billing.entity.ContractProto.*;

public class ContractRequestMapper {
    public static ContractProto.Contract map(ContractRequest request) {
        ContractDataV1.Builder contractDataBuilder = ContractDataV1.newBuilder();
        request.getPositions().forEach(p -> {
            Position.Builder positionBuilder = Position.newBuilder()
                    .setId(p.getId())
                    .setSource(p.getSource())
                    .setOperator(p.getOperatorGroup())
                    .setRecalcRule(RecalcRule.forNumber(p.getRecalcRule()))
                    .setTrafficType(TrafficType.forNumber(p.getTrafficType()))
                    .putAllSteps(p.getSteps()
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    e -> Long.valueOf(e.getKey()),
                                    e -> e.getValue()
                            )));
            if (p.getUnionId() != null) {
                positionBuilder.setUnionId(p.getUnionId());
            }
            contractDataBuilder.addPositions(positionBuilder.build()
            );
        });
        return Contract.newBuilder().setContractDataV1(contractDataBuilder).build();
    }
}
