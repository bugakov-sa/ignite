package presentation.ignite.billing.service;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static presentation.ignite.billing.entity.ClusterObjectNames.*;

@Service
public class SystemService {

    @Autowired
    private Ignite ignite;

    public void clearAllCaches() {
        ignite.cache(LOGIN_CONTRACTS_CACHE).clear();
        ignite.cache(SMS_CONTRACTS_CACHE).clear();
        ignite.cache(SMS_COUNTERS_CACHE).clear();
        ignite.cache(MONEY_COUNTERS_CACHE).clear();
    }
}
