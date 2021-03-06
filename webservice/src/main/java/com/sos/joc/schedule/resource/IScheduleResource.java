
package com.sos.joc.schedule.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleFilter;

public interface IScheduleResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSchedule(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ScheduleFilter scheduleFilterSchema) throws Exception;

}
