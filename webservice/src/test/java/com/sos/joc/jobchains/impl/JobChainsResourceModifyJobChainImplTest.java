package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;

public class JobChainsResourceModifyJobChainImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainsStopTest() throws Exception {

        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);

        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        listOfJobChains.add(jobChain2);

        modifySchema.setJobChains(listOfJobChains);

        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsStop(accessToken, modifySchema);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsStopTest", true, okSchema.getOk());
    }

    @Test
    public void postJobChainsUnStopTest() throws Exception {

        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);

        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        listOfJobChains.add(jobChain2);

        modifySchema.setJobChains(listOfJobChains);

        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsUnStop(accessToken, modifySchema);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsUnStopTest", true, okSchema.getOk());
    }

}