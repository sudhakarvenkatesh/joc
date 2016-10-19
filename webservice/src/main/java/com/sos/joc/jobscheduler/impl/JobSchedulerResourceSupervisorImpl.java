package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSupervisor;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorImpl extends JOCResourceImpl  implements IJobSchedulerResourceSupervisor {


    @Override
    public JOCDefaultResponse postJobschedulerSupervisor(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerId);

        try {
            Globals.beginTransaction();
            DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerResource.getJobschedulerUser().getSchedulerInstance(
                    new JobSchedulerIdentifier(jobSchedulerId.getJobschedulerId()));
            if (dbItemInventoryInstance == null) {
                String errMessage = String.format("jobschedulerId %s not found in table %s", jobSchedulerId.getJobschedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBInvalidDataException(errMessage);
            }

            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
                dbItemInventoryInstance = dbLayer.getInventoryInstancesByKey(supervisorId);

                if (dbItemInventoryInstance == null) {
                    String errMessage = String.format("jobschedulerId for supervisor of %s with internal id %s not found in table %s", jobSchedulerId
                            .getJobschedulerId(), supervisorId, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                }
                jobSchedulerId.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
                jobSchedulerResource.setJobSchedulerFilterSchema(jobSchedulerId);
                return jobSchedulerResource.postJobscheduler();
            } else {
                JobSchedulerV200 entity = new JobSchedulerV200();
                entity.setDeliveryDate(new Date());
                entity.setJobscheduler(new JobSchedulerV());
                return JOCDefaultResponse.responseStatus200(entity);
            }
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } finally {
            Globals.rollback();
        }
    }

}
