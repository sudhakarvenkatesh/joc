package com.sos.joc.order.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.runtime.RunTimeEntity;
import com.sos.joc.order.post.OrderRunTimeBody;
import com.sos.joc.order.resource.IOrderRunTimeResource;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {
    private static final Logger LOGGER = Logger.getLogger(OrderRunTimeResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderRunTimeBody orderRunTimeBody) throws Exception {
        LOGGER.debug("init OrderRunTime");
        JOCDefaultResponse jocDefaultResponse = init(orderRunTimeBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            // TODO JOC Cockpit Webservice
            RunTimeEntity runTimeEntity = new RunTimeEntity();
            return JOCDefaultResponse.responseStatus200(runTimeEntity.getEntity());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
