package presentation.ignite.billing.service;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static presentation.ignite.billing.entity.ContractProto.*;

@Service
public class ContractService {

    private static final Logger log = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    private IgniteCache<String, byte[]> contractsCache;

    public void save(String login, Contract contract) {
        log.info("Сохранение контракта {} - {}", login, contract);
        contractsCache.put(login, contract.toByteArray());
        log.info("Сохранен контракт {} - {}", login, contract);
    }

    public Contract read(String login) {
        log.info("Запрос контракта логина {}", login);
        try {
            byte[] contractData = contractsCache.get(login);
            if (contractData == null) {
                log.info("Не найден контракт логина {}", login);
                return null;
            }
            Contract contract = Contract.parseFrom(contractData);
            log.info("Ответ на запрос контракта логина {} - {}", login, contract);
            return contract;
        } catch (InvalidProtocolBufferException e) {
            log.error("Не удалось распарсить контракт ", e);
            return null;
        }
    }
}
