package presentation.ignite.billing.task;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import presentation.ignite.billing.entity.BilledMessage;
import presentation.ignite.billing.entity.Message;

import java.util.List;
import java.util.stream.Collectors;

public class BillBatchByNodeTask extends ComputeJobAdapter {

    @IgniteInstanceResource
    private Ignite ignite;

    private final List<Message> messagesOfNode;

    public BillBatchByNodeTask(List<Message> messagesOfNode) {
        this.messagesOfNode = messagesOfNode;
    }

    @Override
    public Object execute() throws IgniteException {
        return messagesOfNode.stream().map(this::bill).collect(Collectors.toList());
    }

    private BilledMessage bill(Message message) {
        return new BillSingleMessageTask(ignite).bill(message);
    }
}
