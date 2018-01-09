package presentation.ignite.billing.entity;

public interface ClusterObjectNames {

    String SMS_CONTRACTS_CACHE = "contracts";
    String SMS_COUNTERS_CACHE = "smsCounters";
    String MONEY_COUNTERS_CACHE = "moneyCounters";
    String LOGIN_CONTRACTS_CACHE = "loginContracts";

    String RESET_SMS_COUNTERS_MUTEX = "resetSmsCountersMutex";
    String RESET_SMS_COUNTERS_LAST_TIME = "resetSmsCountersLastTime";
}
