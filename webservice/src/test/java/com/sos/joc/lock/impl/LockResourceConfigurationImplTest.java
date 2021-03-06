package com.sos.joc.lock.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.lock.LockConfigurationFilter;
 
public class LockResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(LockResourceConfigurationImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";
     
    @Test
    public void postLockConfigurationDefaultTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        LockConfigurationFilter lockConfigurationFilterSchema = new LockConfigurationFilter();
        lockConfigurationFilterSchema.setLock("myLock");
        lockConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        LockResourceConfigurationImpl lockResourceConfigurationImpl = new LockResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = lockResourceConfigurationImpl.postLockConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), lockConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postLockConfigurationTest","myPath", configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postLockConfigurationHtmlTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        LockConfigurationFilter lockConfigurationFilterSchema = new LockConfigurationFilter();
        lockConfigurationFilterSchema.setLock("myLock");
        lockConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        lockConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        LockResourceConfigurationImpl lockResourceConfigurationImpl = new LockResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = lockResourceConfigurationImpl.postLockConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), lockConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postLockConfigurationHtmlTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

