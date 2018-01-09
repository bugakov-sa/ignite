package presentation.ignite.billing.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import presentation.ignite.billing.entity.ClusterObjectNames;

import java.util.Optional;

import static presentation.ignite.billing.entity.ClusterObjectNames.MONEY_COUNTERS_CACHE;

@Service
public class BalanceService {

    @Autowired
    private Ignite ignite;

    public long getBalance(long contractId) {
        IgniteCache<Long, Long> moneyCounters = ignite.getOrCreateCache(MONEY_COUNTERS_CACHE);
        return Optional.ofNullable(moneyCounters.get(contractId)).orElse(0L);
    }
}
