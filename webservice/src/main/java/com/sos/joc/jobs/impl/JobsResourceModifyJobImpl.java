package com.sos.joc.jobs.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.resource.IJobsResourceModifyJob;
import com.sos.joc.model.job.ModifyJobSchema;
import com.sos.joc.model.job.ModifyJobsSchema;
import com.sos.scheduler.model.commands.JSCmdModifyJob;
import com.sos.scheduler.model.objects.RunTime;

@Path("jobs")
public class JobsResourceModifyJobImpl extends JOCResourceImpl implements IJobsResourceModifyJob {
    private static final String CONTINUE = "continue";
    private static final String END = "end";
    private static final String SUSPEND = "suspend";
    private static final String STOP = "stop";
    private static final String SET_RUN_TIME = "set_run_time";
    private static final String UNSTOP = "unstop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceModifyJobImpl.class);

    private JOCDefaultResponse executeModifyJobCommand(ModifyJobSchema modifyJobsSchema, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdModifyJob jsCmdModifyJob = Globals.schedulerObjectFactory.createModifyJob();
            jsCmdModifyJob.setCmdIfNotEmpty(command);
            jsCmdModifyJob.setJobIfNotEmpty(modifyJobsSchema.getJob());
            if (SET_RUN_TIME.equals(command)) {
                RunTime runtime = (RunTime) Globals.schedulerObjectFactory.unMarshall(modifyJobsSchema.getRunTime());
                jsCmdModifyJob.setRunTime(runtime);
            }
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdModifyJob);

            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing job.%s %s:%s",command, e.getCause(), e.getMessage()));
        }
    }

    private JOCDefaultResponse postJobsCommand(String accessToken, String command, boolean permission, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/stop");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, modifyJobsSchema.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyJobSchema modifyJobschema : modifyJobsSchema.getJobs()) {
                jocDefaultResponse = executeModifyJobCommand(modifyJobschema, command);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;
    }

    @Override
    public JOCDefaultResponse postJobsStop(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/stop");
        return postJobsCommand(accessToken, STOP, getPermissons(accessToken).getJob().isStop(), modifyJobsSchema);
    }

    @Override
    public JOCDefaultResponse postJobsUnstop(String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception {
        LOGGER.debug("init jobs/unstop");
        return postJobsCommand(accessToken, UNSTOP, getPermissons(accessToken).getJob().isUnstop(), modifyJobsSchema);
    }

    @Override
    public JOCDefaultResponse postJobsSetRunTime(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/set_run_time");
        return postJobsCommand(accessToken, SET_RUN_TIME, getPermissons(accessToken).getJob().isSetRunTime(), modifyJobsSchema);
    }

    @Override
    public JOCDefaultResponse postJobsEndAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/end_all_tasks");
        return postJobsCommand(accessToken, END, getPermissons(accessToken).getJob().isEndAllTasks(), modifyJobsSchema);
    }

    @Override
    public JOCDefaultResponse postJobsSuspendAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init job/suspend_all_tasks");
        return postJobsCommand(accessToken, SUSPEND, getPermissons(accessToken).getJob().isSuspendAllTasks(), modifyJobsSchema);
    }

    @Override
    public JOCDefaultResponse postJobsContinueAllTasks(String accessToken, ModifyJobsSchema modifyJobsSchema) {
        LOGGER.debug("init jobs/continue_all_tasks");
        return postJobsCommand(accessToken, CONTINUE, getPermissons(accessToken).getJob().isContinueAllTasks(), modifyJobsSchema);
    }

}