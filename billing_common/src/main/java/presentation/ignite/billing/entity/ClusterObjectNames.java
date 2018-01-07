package presentation.ignite.billing.entity;

public interface ClusterObjectNames {

    String SMS_CONTRACTS_CACHE = "smsContracts";
    String SMS_COUNTERS_CACHE = "smsCounters";
    String MONEY_COUNTERS_CACHE = "moneyCounters";

    String RESET_SMS_COUNTERS_MUTEX = "resetSmsCountersMutex";
    String RESET_SMS_COUNTERS_LAST_TIME = "resetSmsCountersLastTime";
}
