package presentation.ignite.billing.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import presentation.ignite.billing.entity.ClusterObjectNames;


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
                .setName(ClusterObjectNames.SMS_CONTRACTS_CACHE)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1)
                .setOnheapCacheEnabled(true));
    }

    @Bean
    public IgniteCache<BinaryObject, Long> smsCounters(Ignite ignite) {
        return ignite.getOrCreateCache(new CacheConfiguration()
                .setName(ClusterObjectNames.SMS_COUNTERS_CACHE)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1)
                .setOnheapCacheEnabled(true)
                .setKeyConfiguration(new CacheKeyConfiguration()
                        .setTypeName("java.lang.String")
                        .setAffinityKeyFieldName("login")));
    }

    @Bean
    public IgniteCache<String, Long> moneyCounters(Ignite ignite) {
        return ignite.getOrCreateCache(new CacheConfiguration()
                .setName(ClusterObjectNames.MONEY_COUNTERS_CACHE)
                .setCacheMode(CacheMode.PARTITIONED)
                .setBackups(1)
                .setOnheapCacheEnabled(true));
    }
}
