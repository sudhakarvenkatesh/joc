package com.sos.joc.event.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Path;

import org.apache.shiro.session.Session;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.event.EventCallable;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.IEventResource;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.event.JobSchedulerEvent;
import com.sos.joc.model.event.JobSchedulerEvents;
import com.sos.joc.model.event.JobSchedulerObjects;
import com.sos.joc.model.event.RegisterEvent;

@Path("events")
public class EventResourceImpl extends JOCResourceImpl implements IEventResource {

    private static final String API_CALL = "./events";
    private static final int EVENT_TIMEOUT = 90;
    private static final String HTTP_CLIENT_PROPKEY = "events";

    @Override
    public JOCDefaultResponse postEvent(String accessToken, RegisterEvent eventBody) throws Exception {

        JobSchedulerEvents entity = new JobSchedulerEvents();
        Map<String, JobSchedulerEvent> eventList = new HashMap<String, JobSchedulerEvent>();
        
        try {
            initLogging(API_CALL, eventBody);
            boolean perms = getPermissons(accessToken).getJobschedulerMaster().getView().isStatus();

            Session session = getJobschedulerUser().getSosShiroCurrentUser().getCurrentSubject().getSession();
            forceClosingHttpClients(session);

            if (eventBody.getClose() != null && eventBody.getClose()) {
                entity.setEvents(null);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);
            }

            if (eventBody.getJobscheduler() == null && eventBody.getJobscheduler().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'jobscheduler'");
            }

            Long defaultEventId = Instant.now().toEpochMilli() * 1000;
            List<EventCallable> tasks = new ArrayList<EventCallable>();
            List<JOCJsonCommand> jocJsonCommands = new ArrayList<JOCJsonCommand>();
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            Globals.beginTransaction();

            for (JobSchedulerObjects jsObject : eventBody.getJobscheduler()) {
                JOCDefaultResponse jocDefaultResponse = init(accessToken, jsObject.getJobschedulerId(), perms);
                if (jocDefaultResponse != null) {
                    return jocDefaultResponse;
                }
                if (jsObject.getEventId() == null || jsObject.getEventId().isEmpty()) {
                    jsObject.setEventId(defaultEventId.toString());
                }
                JobSchedulerEvent jsEvent = new JobSchedulerEvent();
                jsEvent.setEventId(jsObject.getEventId());
                jsEvent.setJobschedulerId(jsObject.getJobschedulerId());
                eventList.put(jsObject.getJobschedulerId(), jsEvent);
                DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(jsObject.getJobschedulerId(), accessToken);
                JOCJsonCommand command = new JOCJsonCommand();
                command.setUriBuilderForEvents(instance.getUrl());
                command.setSocketTimeout((EVENT_TIMEOUT + 5) * 1000);
                command.createHttpClient();
                command.setAutoCloseHttpClient(false);
                command.addEventQuery(jsObject.getEventId(), EVENT_TIMEOUT);
                jocJsonCommands.add(command);
                tasks.add(new EventCallable(command, jsEvent, accessToken));
            }
            session.setAttribute(HTTP_CLIENT_PROPKEY, jocJsonCommands);

            ExecutorService executorService = Executors.newFixedThreadPool(eventBody.getJobscheduler().size());
            try {
                JobSchedulerEvent evt = executorService.invokeAny(tasks);
                eventList.put(evt.getJobschedulerId(), evt);
            } catch (ExecutionException | InterruptedException e) {
                if (e.getCause() instanceof JocException) {
                    throw (JocException) e.getCause();
                } else {
                    throw (Exception) e.getCause();
                }
            } finally {
                executorService.shutdown(); 
            }

            forceClosingHttpClients(session);

            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (ForcedClosingHttpClientException e) {
            entity.setEvents(new ArrayList<JobSchedulerEvent>(eventList.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    private void forceClosingHttpClients(Session session) {
        if (session.getAttribute(HTTP_CLIENT_PROPKEY) != null) {
            try {
                for (JOCJsonCommand command : (List<JOCJsonCommand>) session.getAttribute(HTTP_CLIENT_PROPKEY)) {
                    command.forcedClosingHttpClient();
                }
                session.removeAttribute(HTTP_CLIENT_PROPKEY);
            } catch (Exception e) {}
        }
    }
}