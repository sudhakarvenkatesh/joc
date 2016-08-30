package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobs.post.JobsBody;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.JobsVSchema;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskQueue;
import com.sos.joc.model.job.RunningTask.Cause;
import com.sos.joc.model.job.OrderQueue.Type;
import com.sos.joc.model.job.OrdersSummary;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = Logger.getLogger(JobsResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsBody jobsBody) throws Exception {
        LOGGER.debug("init Jobs");
        JOCDefaultResponse jocDefaultResponse = init(jobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();

            entity.setDeliveryDate(new Date());

            // TODO Use correct url

            Job_ job = new Job_();

            job.setName("myName");
            job.setPath("myPath");

            com.sos.joc.model.job.State state = new com.sos.joc.model.job.State();
            state.setSeverity(0);
            state.setText(com.sos.joc.model.job.State.Text.pending);
            job.setState(state);
            job.setStateText("myStateText");
            job.setSurveyDate(new Date());
            OrdersSummary ordersSummary = new OrdersSummary();
            ordersSummary.setPending(-1);
            ordersSummary.setRunning(-1);
            ordersSummary.setSetback(-1);
            ordersSummary.setSuspended(-1);
            ordersSummary.setWaitingForResource(-1);
            job.setOrdersSummary(ordersSummary);

            job.setNumOfQueuedTasks(-1);
            job.setNumOfRunningTasks(-1);

            ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
            configurationStatusSchema.setMessage("myMessage");
            configurationStatusSchema.setSeverity(-1);
            configurationStatusSchema.setText(Text.changed_file_not_loaded);
            job.setConfigurationStatus(configurationStatusSchema);
            List<Lock_> listOfLocks = new ArrayList<Lock_>();
            Lock_ lock = new Lock_();
            lock.setExclusive(false);
            lock.setAvailable(true);
            lock.setPath("myPath");
            listOfLocks.add(lock);
            Lock_ lock2 = new Lock_();
            lock2.setExclusive(true);
            lock2.setAvailable(false);
            lock2.setPath("myPath2");
            listOfLocks.add(lock2);
            job.setLocks(listOfLocks);

            
            
             if (jobsBody.getCompact()) {
                job.setAllSteps(-1);
                job.setAllTasks(-1);

                job.setDelayUntil(new Date());

                job.setNextPeriodBegin("myNextPeriodBegin");
                job.setNextStartTime(new Date());

                List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();

                OrderQueue orderQueue = new OrderQueue();

                entity.setDeliveryDate(new Date());
                ConfigurationStatusSchema configurationStatus = new ConfigurationStatusSchema();
                configurationStatus.setMessage("myMessage");
                configurationStatus.setSeverity(0);
                configurationStatus.setText(Text.changed_file_not_loaded);
                orderQueue.setConfigurationStatus(configurationStatus);

                orderQueue.setEndState("myEndState");
                orderQueue.setHistoryId(-1);
                orderQueue.setInProcessSince(new Date());
                orderQueue.setJob("myJob");
                orderQueue.setJobChain("myJobChain");
                orderQueue.setLock("myLock");
                orderQueue.setNextStartTime(new Date());
                orderQueue.setOrderId("myOrderId");

                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);
                orderQueue.setParams(parameters);

                orderQueue.setPath("myPath");
                orderQueue.setPriority(-1);
                orderQueue.setProcessClass("myProcessClass");
                orderQueue.setProcessedBy("myProcessedBy");

                ProcessingState processingState = new ProcessingState();
                processingState.setSeverity(1);
                processingState.setText(ProcessingState.Text.running);

                orderQueue.setProcessingState(processingState);

                orderQueue.setSetback(new Date());
                orderQueue.setStartedAt(new Date());
                orderQueue.setState("myState");
                orderQueue.setStateText("myStateText");
                orderQueue.setSurveyDate(new Date());
                orderQueue.setTaskId(-1);
                orderQueue.setType(Type.file_order);
                listOrderQueue.add(orderQueue);
                job.setOrderQueue(listOrderQueue);

                job.setParams(parameters);

                List<RunningTask> listOfRunningTask = new ArrayList<RunningTask>();
                RunningTask runningTask = new RunningTask();
                runningTask.setCause(Cause.none);
                runningTask.setEnqueued(new Date());
                runningTask.setIdleSince(new Date());

                Order order = new Order();
                order.setInProcessSince(new Date());
                order.setJobChain("myJobChain");
                order.setOrderId("myOrderId");
                order.setPath("myPath");
                order.setState("myState");
                runningTask.setOrder(order);

                runningTask.setPid(-1);
                runningTask.setStartedAt(new Date());
                runningTask.setSteps(-1);
                runningTask.setTaskId(-1);
                listOfRunningTask.add(runningTask);

                job.setRunningTasks(listOfRunningTask);

                List<TaskQueue> listOfTasks = new ArrayList<TaskQueue>();
                TaskQueue taskQueue = new TaskQueue();
                taskQueue.setTaskId(-1);
                job.setTaskQueue(listOfTasks);

                job.setTemporary(false);
            }
            
            listJobs.add(job);
            entity.setJobs(listJobs);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
}
