package presentation.ignite.billing.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoneyService {

    @Autowired
    private Ignite ignite;

    public long getMoneyDebt(String login) {
        IgniteCache<String, Long> moneyCounters = ignite.getOrCreateCache("moneyCounters");
        return Optional.ofNullable(moneyCounters.get(login)).orElse(0L);
    }
}
