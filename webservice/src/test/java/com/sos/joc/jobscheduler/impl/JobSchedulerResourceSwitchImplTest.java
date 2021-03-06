package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.Ok;

public class JobSchedulerResourceSwitchImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerSwitchTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceSwitchImpl jobSchedulerResourceSwitchImpl= new JobSchedulerResourceSwitchImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobSchedulerResourceSwitchImpl.postJobschedulerSwitch(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        Ok okSchema = (Ok) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerSwitchTest", true, okSchema.getOk());
     }

}

