package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import presentation.ignite.billing.service.MoneyService;

@RestController
public class MoneyController {

    @Autowired
    private MoneyService moneyService;

    @GetMapping("money")
    public long getMoneyDebt(@RequestParam String login) {
        return moneyService.getMoneyDebt(login);
    }
}
