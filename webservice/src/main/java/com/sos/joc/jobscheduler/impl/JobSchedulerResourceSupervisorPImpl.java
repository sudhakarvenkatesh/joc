package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourcePSupervisor;
import com.sos.joc.model.common.JobSchedulerFilterSchema;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorPImpl  implements IJobSchedulerResourcePSupervisor {
 

    @Override
    public JOCDefaultResponse postJobschedulerSupervisorP(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerFilterSchema);

        DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerPResource.getJobschedulerUser().getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerFilterSchema.getJobschedulerId()));  
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s",jobSchedulerFilterSchema. getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
        }

        jobSchedulerFilterSchema.setJobschedulerId(dbItemInventoryInstance.getSupervisorId());
        return jobSchedulerPResource.postJobschedulerP();


    }

}