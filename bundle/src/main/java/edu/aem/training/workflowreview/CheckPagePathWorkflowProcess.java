package edu.aem.training.workflowreview;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.Route;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.util.List;


@Component
@Service(WorkflowProcess.class)
@Property(name="process.label", value="AEM Check Page Path Workflow")
public class CheckPagePathWorkflowProcess implements WorkflowProcess {

    private static Logger log =
            LoggerFactory.getLogger(CheckPagePathWorkflowProcess.class);

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        log.info("CheckPagePathWorkflowProcess executed");
        log.info("Payload type: " + workItem.getWorkflowData().getPayloadType());
        log.info("Payload: " + workItem.getWorkflowData().getPayload());

        String pagePath = workItem.getWorkflowData().getPayload().toString();

        try {
            // Create a node that represents the root node
            Node root = workflowSession.getSession().getRootNode();
            // Get page node from root
            Node pageNode = root.getNode(pagePath.substring(1));

            Node pageContent = pageNode.getNode("jcr:content");

            if(!pageContent.hasProperty("pathToMove")) {
                log.info("jcr:content's property pathToMove is not present");
            } else {
                log.info("jcr:content's property pathToMove is present");
                workflowSession.terminateWorkflow(workItem.getWorkflow());
                log.info("Workflow is terminated by step 1");
                return;
            }

        } catch (Exception e) {
            log.info("ERROR: " + e.getMessage());
        }

        log.info("Go to Next Step");
    }
}
