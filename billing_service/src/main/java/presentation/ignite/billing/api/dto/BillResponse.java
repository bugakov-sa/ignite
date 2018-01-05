package presentation.ignite.billing.api.dto;

public class BillResponse {
    private final long messageId;
    private final int billingResult;

    public BillResponse(long messageId, int billingResult) {
        this.messageId = messageId;
        this.billingResult = billingResult;
    }

    public long getMessageId() {
        return messageId;
    }

    public int getBillingResult() {
        return billingResult;
    }
}
