package presentation.ignite.billing.entity;

public class BilledMessage {
    private final long messageId;
    private final BillingResult billingResult;

    public BilledMessage(long messageId, BillingResult billingResult) {
        this.messageId = messageId;
        this.billingResult = billingResult;
    }

    public long getMessageId() {
        return messageId;
    }

    public BillingResult getBillingResult() {
        return billingResult;
    }
}
