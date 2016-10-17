package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.jobscheduler.AgentVCallable;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.AgentsV;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.order.OrderV;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgents(String accessToken, AgentFilter agentFilterSchema) {
        LOGGER.debug("init jobscheduler/agents");
        try {
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(dbItemInventoryInstance.getUrl(), WebserviceConstants.AGENTS_API_LIST_PATH );
            JsonObject json = jocJsonCommand.getJsonObjectFromGet();
            JsonArray agentUris = json.getJsonArray("elements");
            List<AgentV> listOfAgents = new ArrayList<AgentV>();
            List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
            for (JsonString agentUri : agentUris.getValuesAs(JsonString.class)) {
                tasks.add(new AgentVCallable(agentUri.toString(), dbItemInventoryInstance.getUrl()));
            }
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<AgentV> result : executorService.invokeAll(tasks)) {
                listOfAgents.add(result.get());
            }
            
            AgentsV entity = new AgentsV();
            entity.setAgents(listOfAgents);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
