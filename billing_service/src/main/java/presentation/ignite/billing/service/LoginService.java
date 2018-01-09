package presentation.ignite.billing.service;

import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private IgniteCache<String, Long> loginContracts;

    public long getContractId(String login) {
        return Optional.ofNullable(loginContracts.get(login)).orElse(0L);
    }

    public void setContractId(String login, long contractId) {
        loginContracts.put(login, contractId);
    }
}
