package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrdersFilter;

public class OrdersResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersHistory() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId("scheduler_current");
        OrdersResourceHistoryImpl ordersResourceHistoryImpl = new OrdersResourceHistoryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersHistory(sosShiroCurrentUserAnswer.getAccessToken(), ordersFilterSchema);
        OrderHistory historySchema = (OrderHistory) ordersResponse.getEntity();
        assertEquals("postOrdersHistory","myPath", historySchema.getHistory().get(0).getPath());
     }

}

