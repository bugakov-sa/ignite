package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import presentation.ignite.billing.service.SystemService;

@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("system/clear")
    public void clearAllCaches() {
        systemService.clearAllCaches();
    }
}
