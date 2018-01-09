package presentation.ignite.billing.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cluster.ClusterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import presentation.ignite.billing.entity.BilledMessage;
import presentation.ignite.billing.entity.Message;
import presentation.ignite.billing.task.BillBatchByClusterTask;

import java.util.List;

import static presentation.ignite.billing.entity.ClusterObjectNames.SMS_CONTRACTS_CACHE;

@Service
public class BillingService {

    @Autowired
    private Ignite ignite;

    public List<BilledMessage> bill(List<Message> messages) {
        ClusterGroup clusterGroup = ignite.cluster().forDataNodes(SMS_CONTRACTS_CACHE);
        return ignite.compute(clusterGroup).execute(new BillBatchByClusterTask(), messages);
    }
}
