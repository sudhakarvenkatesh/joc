package com.sos.joc.processclasses.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;
import com.sos.joc.processClasses.impl.ProcessClassesResourcePImpl;

public class ProcessClassesResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postProcessClassesTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ProcessClassesFilter processClassesFilterSchema = new ProcessClassesFilter();
        processClassesFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ProcessClassesResourcePImpl processClassesResourcePImpl = new ProcessClassesResourcePImpl();
        JOCDefaultResponse jobsResponse = processClassesResourcePImpl.postProcessClassesP(sosShiroCurrentUserAnswer.getAccessToken(), processClassesFilterSchema);
        ProcessClassesP processClassesPSchema = (ProcessClassesP) jobsResponse.getEntity();
        assertEquals("postProcessClassesTest", "myName", processClassesPSchema.getProcessClasses().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

}
