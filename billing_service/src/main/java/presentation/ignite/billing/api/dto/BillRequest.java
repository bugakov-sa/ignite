package presentation.ignite.billing.api.dto;

public class BillRequest {
    private long messageId;
    private String login;
    private String source;
    private String operatorGroup;
    private int trafficType;

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setOperatorGroup(String operatorGroup) {
        this.operatorGroup = operatorGroup;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public long getMessageId() {
        return messageId;
    }

    public String getLogin() {
        return login;
    }

    public String getSource() {
        return source;
    }

    public String getOperatorGroup() {
        return operatorGroup;
    }

    public int getTrafficType() {
        return trafficType;
    }
}
