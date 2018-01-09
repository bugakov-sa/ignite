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
    private IgniteCache<Long, byte[]> contractsCache;

    public void save(long contractId, Contract contract) {
        log.info("Сохранение контракта {} - {}", contractId, contract);
        contractsCache.put(contractId, contract.toByteArray());
        log.info("Сохранен контракт {} - {}", contractId, contract);
    }

    public Contract read(long contractId) {
        log.info("Запрос контракта {}", contractId);
        try {
            byte[] contractData = contractsCache.get(contractId);
            if (contractData == null) {
                log.info("Не найден контракт {}", contractId);
                return null;
            }
            Contract contract = Contract.parseFrom(contractData);
            log.info("Ответ на запрос контракта {} - {}", contractId, contract);
            return contract;
        } catch (InvalidProtocolBufferException e) {
            log.error("Не удалось распарсить контракт ", e);
            return null;
        }
    }
}
