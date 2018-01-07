package presentation.ignite.billing.task;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
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
import java.util.stream.Collectors;

import static presentation.ignite.billing.entity.ClusterObjectNames.SMS_CONTRACTS_CACHE;

public class BillBatchByClusterTask extends ComputeTaskAdapter<List<Message>, List<BilledMessage>> {

    @IgniteInstanceResource
    private Ignite ignite;

    @Nullable
    @Override
    public Map<? extends ComputeJob, ClusterNode> map(
            List<ClusterNode> subgrid,
            @Nullable List<Message> arg
    ) throws IgniteException {

        return arg.stream().collect(Collectors.groupingBy(
                this::findNodeByLogin,
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        messagesOfNode -> new BillBatchByNodeTask(messagesOfNode)
                )
        )).entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getValue,
                Map.Entry::getKey
        ));
    }

    private ClusterNode findNodeByLogin(Message message) {
        return ignite.affinity(SMS_CONTRACTS_CACHE).mapKeyToNode(message.getLogin());
    }

    @Nullable
    @Override
    public List<BilledMessage> reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream()
                .flatMap(r -> ((List<BilledMessage>) r.getData()).stream())
                .collect(Collectors.toList());
    }
}
