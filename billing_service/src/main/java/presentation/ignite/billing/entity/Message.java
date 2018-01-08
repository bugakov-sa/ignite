package presentation.ignite.billing.entity;

import presentation.ignite.billing.entity.ContractProto.TrafficType;

import java.io.Serializable;

public class Message implements Serializable {

    static final long serialVersionUID = 1L;

    private final long id;
    private final String login;
    private final String source;
    private final String operator;
    private final TrafficType trafficType;

    public Message(long id, String login, String source, String operator, TrafficType trafficType) {
        this.id = id;
        this.login = login;
        this.source = source;
        this.operator = operator;
        this.trafficType = trafficType;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSource() {
        return source;
    }

    public String getOperator() {
        return operator;
    }

    public TrafficType getTrafficType() {
        return trafficType;
    }
}
