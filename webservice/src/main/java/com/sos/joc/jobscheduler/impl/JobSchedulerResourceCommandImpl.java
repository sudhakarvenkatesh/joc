package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCommand;
import com.sos.joc.model.commands.JobschedulerCommand;

@Path("jobscheduler")
public class JobSchedulerResourceCommandImpl extends JOCResourceImpl implements IJobSchedulerResourceCommand {

    private static final String API_CALL = "./jobscheduler/command";

    @Override
    public JOCDefaultResponse postJobschedulerCommand(String accessToken, JobschedulerCommand jobschedulerCommand) {

        try {
            initLogging(API_CALL, jobschedulerCommand.getJobschedulerId());
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobschedulerCommand.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", jobschedulerCommand.getJobschedulerId());
            if ("".equals(jobschedulerCommand.getUrl()) || jobschedulerCommand.getUrl() == null){
                jobschedulerCommand.setUrl(dbItemInventoryInstance.getUrl());
            }

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() > 1){
                JocError e = new JocError();
                e.setCode(WebserviceConstants.COMMAND_ERROR_CODE);
                e.setMessage(String.format("There are %s commands specified. Only one command is allowed",jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size()));
                throw new JocException(e);
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(jobschedulerCommand.getUrl());

            JobSchedulerCommandFactory jobSchedulerCommandFactory = new JobSchedulerCommandFactory();

            if (jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().size() < 1){
                getJocError().setCode(WebserviceConstants.COMMAND_ERROR_CODE);
                getJocError().setMessage("Unknown command");
            }
            
            String xml = jobSchedulerCommandFactory.getXml(jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().get(0));
            String answer = jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());

            return JOCDefaultResponse.responseStatus200(answer, "application/xml");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }

}