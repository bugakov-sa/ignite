package presentation.ignite.billing.task;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.jetbrains.annotations.Nullable;
import presentation.ignite.billing.entity.BilledMessage;
import presentation.ignite.billing.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static presentation.ignite.billing.entity.ClusterObjectNames.LOGIN_CONTRACTS_CACHE;
import static presentation.ignite.billing.entity.ClusterObjectNames.SMS_CONTRACTS_CACHE;

public class BillBatchByClusterTask extends ComputeTaskAdapter<List<Message>, List<BilledMessage>> {

    @IgniteInstanceResource
    private Ignite ignite;

    @Nullable
    @Override
    public Map<? extends ComputeJob, ClusterNode> map(
            List<ClusterNode> subgrid,
            @Nullable List<Message> messages
    ) throws IgniteException {

        IgniteCache<String, Long> loginContracts = ignite.cache(LOGIN_CONTRACTS_CACHE);
        Set<String> logins = messages.stream().map(Message::getLogin).collect(Collectors.toSet());
        Map<String, Long> loginContractsMap = loginContracts.getAll(logins);

        messages.forEach(message -> {
            Long contractId = loginContractsMap.getOrDefault(message.getLogin(), 0L);
            message.setContractId(contractId);
        });

        Affinity<Long> contractsAffinity = ignite.affinity(SMS_CONTRACTS_CACHE);

        Map<ClusterNode, List<Message>> messagesGroupedByNodes = messages.stream().collect(Collectors.groupingBy(
                message -> contractsAffinity.mapKeyToNode(message.getContractId())
        ));
        return messagesGroupedByNodes.entrySet().stream().collect(Collectors.toMap(
                entry -> new BillBatchByNodeTask(entry.getValue()),
                Map.Entry::getKey
        ));
    }

    @Nullable
    @Override
    public List<BilledMessage> reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream()
                .flatMap(r -> ((List<BilledMessage>) r.getData()).stream())
                .collect(Collectors.toList());
    }
}
