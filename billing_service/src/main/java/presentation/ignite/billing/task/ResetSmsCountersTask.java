package presentation.ignite.billing.task;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import presentation.ignite.billing.entity.ClusterObjectNames;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

@Component
public class ResetSmsCountersTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ResetSmsCountersTask.class);

    private static final String RESET_CRON_EXPRESSION = "0 0 1 * *";
    private static final long RESET_MAX_DURATION = TimeUnit.DAYS.toMillis(1);

    @Autowired
    private Ignite ignite;

    @PostConstruct
    public void schedule() {
        log.info("Планирование сброса счетчиков");
        ignite.scheduler().scheduleLocal(this,RESET_CRON_EXPRESSION);
    }

    @Override
    public void run() {
        IgniteLock lock = ignite.reentrantLock(
                ClusterObjectNames.RESET_SMS_COUNTERS_MUTEX,
                true,
                false,
                true
        );
        lock.lock();
        log.info("Сброс счетчиков");
        try {
            IgniteAtomicLong lastResetTime = ignite.atomicLong(
                    ClusterObjectNames.RESET_SMS_COUNTERS_LAST_TIME,
                    0,
                    true
            );
            if(anotherClientAlreadyReseted(lastResetTime)) {
                log.info("Счетчики уже сбросил другой клиент");
            }
            else {
                ignite.cache(ClusterObjectNames.SMS_COUNTERS_CACHE).clear();
                log.info("Счетчики сброшены");
                lastResetTime.getAndSet(currentTimeMillis());
            }

        } catch (Throwable e) {
            log.error("Не удалось сбросить счетчики", e);
        } finally {
            lock.unlock();
        }
    }

    private boolean anotherClientAlreadyReseted(IgniteAtomicLong lastResetTime) {
        return currentTimeMillis() - lastResetTime.get() < RESET_MAX_DURATION;
    }
}
