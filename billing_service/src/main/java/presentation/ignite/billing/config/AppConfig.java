package presentation.ignite.billing.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.EvictionFilter;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import presentation.ignite.billing.entity.CacheNames;
import presentation.ignite.billing.entity.MessageCounterKey;

import javax.cache.Cache;

@Configuration
public class AppConfig {
    @Bean
    public Ignite ignite() {
        Ignite ignite = Ignition.start(new IgniteConfiguration()
                .setClientMode(true)
                .setPeerClassLoadingEnabled(true));
        ignite.active(true);
        return ignite;
    }

    @Bean
    public IgniteCache<String, byte[]> smsContracts(Ignite ignite) {
        return ignite.getOrCreateCache(new CacheConfiguration()
                .setName(CacheNames.SMS_CONTRACTS)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1));
    }

    @Bean
    public IgniteCache<MessageCounterKey, Long> smsCounters(Ignite ignite) {
        return ignite.getOrCreateCache(new CacheConfiguration()
                .setName(CacheNames.SMS_COUNTERS)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1));
    }

    @Bean
    public IgniteCache<String, Long> moneyCounters(Ignite ignite) {
        return ignite.getOrCreateCache(new CacheConfiguration()
                .setName(CacheNames.MONEY_COUNTERS)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1));
    }
}
