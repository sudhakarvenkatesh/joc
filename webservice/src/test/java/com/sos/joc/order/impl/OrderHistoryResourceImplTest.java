package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.model.order.OrderStepHistory;

public class OrderHistoryResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderHistoryFilter orderFilterSchema = new OrderHistoryFilter();
        orderFilterSchema.setJobChain("Cluster/cluster/job_chain1");
        orderFilterSchema.setOrderId("8");
        orderFilterSchema.setJobschedulerId("scheduler_joc_cockpit");
        orderFilterSchema.setHistoryId("690");
        OrderHistoryResourceImpl orderHistoryImpl = new OrderHistoryResourceImpl();
        JOCDefaultResponse ordersResponse = orderHistoryImpl.postOrderHistory(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterSchema);
        OrderStepHistory stepHistorySchema = (OrderStepHistory) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest",2, stepHistorySchema.getHistory().getSteps().get(1).getStep().intValue());
     }

}

