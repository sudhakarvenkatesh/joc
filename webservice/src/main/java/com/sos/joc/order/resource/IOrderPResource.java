
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderFilter;

public interface IOrderPResource {

    @POST
    @Path("p")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderP(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrderFilter orderFilter) throws Exception;
}
