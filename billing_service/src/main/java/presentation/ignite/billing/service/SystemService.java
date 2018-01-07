package presentation.ignite.billing.service;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import presentation.ignite.billing.entity.ClusterObjectNames;

@Service
public class SystemService {

    @Autowired
    private Ignite ignite;

    public void clearAllCaches() {
        ignite.cache(ClusterObjectNames.SMS_CONTRACTS_CACHE).clear();
        ignite.cache(ClusterObjectNames.SMS_COUNTERS_CACHE).clear();
        ignite.cache(ClusterObjectNames.MONEY_COUNTERS_CACHE).clear();
    }
}
