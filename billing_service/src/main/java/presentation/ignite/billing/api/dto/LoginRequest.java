package presentation.ignite.billing.api.dto;

public class LoginRequest {
    private String login;
    private long contractId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }
}
