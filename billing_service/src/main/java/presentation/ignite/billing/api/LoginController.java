package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import presentation.ignite.billing.api.dto.LoginRequest;
import presentation.ignite.billing.service.LoginService;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("login/contract")
    public long getContractId(@RequestParam String login) {
        return loginService.getContractId(login);
    }

    @PostMapping("login/contract")
    public void setContract(@RequestBody LoginRequest request) {
        loginService.setContractId(request.getLogin(), request.getContractId());
    }
}
