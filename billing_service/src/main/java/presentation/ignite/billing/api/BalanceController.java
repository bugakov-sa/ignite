package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import presentation.ignite.billing.service.BalanceService;

@RestController
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("balance")
    public long getBalance(@RequestParam long contractId) {
        return balanceService.getBalance(contractId);
    }
}
