package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;

public class OrdersResourceOverviewSnapshotImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrdersOverviewSnapshot() throws Exception {

        JobChainsFilter filterSchema = new JobChainsFilter();
        filterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrdersResourceOverviewSnapshotImpl ordersResourceOverviewSnapshotImpl = new OrdersResourceOverviewSnapshotImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSnapshotImpl.postOrdersOverviewSnapshot(accessToken, filterSchema);
        OrdersSnapshot snapshotSchema = (OrdersSnapshot) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSnapshot", 0, snapshotSchema.getOrders().getRunning().intValue());
    }

}
