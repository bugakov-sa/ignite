package presentation.ignite.billing.entity;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.io.Serializable;

public final class MessageCounterKey implements Serializable {

    static final long serialVersionUID = 0L;

    @AffinityKeyMapped
    private final String login;

    private final long counterId;

    public MessageCounterKey(String login, long counterId) {
        this.login = login;
        this.counterId = counterId;
    }

    public String getLogin() {
        return login;
    }

    public long getCounterId() {
        return counterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageCounterKey that = (MessageCounterKey) o;

        return counterId == that.counterId;
    }

    @Override
    public int hashCode() {
        return (int) (counterId ^ (counterId >>> 32));
    }
}
