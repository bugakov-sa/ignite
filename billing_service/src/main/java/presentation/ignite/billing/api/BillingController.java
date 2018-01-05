package presentation.ignite.billing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import presentation.ignite.billing.api.dto.BillRequest;
import presentation.ignite.billing.api.dto.BillResponse;
import presentation.ignite.billing.api.dto.mapper.BillRequestMapper;
import presentation.ignite.billing.api.dto.mapper.BillResponseMapper;
import presentation.ignite.billing.entity.BilledMessage;
import presentation.ignite.billing.entity.Message;
import presentation.ignite.billing.service.BillingService;

import java.util.List;

@RestController
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("bill2")
    @ResponseBody
    public List<BillResponse> bill(@RequestBody List<BillRequest> request) {
        List<Message> messages = BillRequestMapper.map(request);
        List<BilledMessage> billedMessages = billingService.bill(messages);
        return BillResponseMapper.map(billedMessages);
    }
}
