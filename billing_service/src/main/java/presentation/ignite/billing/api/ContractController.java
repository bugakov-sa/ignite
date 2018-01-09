package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import presentation.ignite.billing.api.dto.ContractRequest;
import presentation.ignite.billing.api.dto.mapper.ContractRequestMapper;
import presentation.ignite.billing.service.ContractService;

import java.util.Optional;

import static presentation.ignite.billing.entity.ContractProto.*;

@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("contract")
    public void saveContract(@RequestBody ContractRequest request) {
        Contract contract = ContractRequestMapper.map(request);
        contractService.save(request.getId(), contract);
    }

    @GetMapping("contract")
    public String readContract(@RequestParam long contractId) {
        return Optional.ofNullable(contractService.read(contractId))
                .map(Contract::toString)
                .orElse(null);
    }
}
