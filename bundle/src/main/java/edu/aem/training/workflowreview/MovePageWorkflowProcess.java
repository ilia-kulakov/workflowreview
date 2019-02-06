package edu.aem.training.workflowreview;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;


@Component
@Service(WorkflowProcess.class)
@Property(name="process.label", value="AEM Move Page Workflow")
public class MovePageWorkflowProcess implements WorkflowProcess {

    private static Logger log =
            LoggerFactory.getLogger(MovePageWorkflowProcess.class);

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        log.info("MovePageWorkflowProcess executed");
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
                workflowSession.terminateWorkflow(workItem.getWorkflow());
                log.info("Workflow is terminated by step 3");
                return;
            } else {
                String pathToMove = pageContent.getProperty("pathToMove").getString();
                log.info("jcr:content/pathToMove is present:"  + pathToMove);
                if(root.hasNode(pathToMove.substring(1))) {
                    log.info("Node "  + pathToMove + " is valid");
                    String baseFolder = pagePath.substring(0, pagePath.lastIndexOf("/"));
                    String pageName = pagePath.substring(baseFolder.length() + 1);
                    log.info("Base folder: " + baseFolder);

                    if(!pathToMove.equals(baseFolder)) {
                        log.info("Begin to move the page...");
                        String newPagePath = pathToMove + "/" + pageName;
                        Session session =  workflowSession.getSession();
                        session.move(pagePath, newPagePath);
                        log.info("SUCCESS: Page was moved to " + newPagePath);
                    } else {
                        log.info("pathToMove matches the current page path");
                    }
                } else {
                    log.info("Node "  + pathToMove + " is invalid");
                }
            }

        } catch (Exception e) {
            log.info("ERROR: " + e.getMessage());
        }
    }
}
