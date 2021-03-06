
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskHistoryFilter;
 
public interface IJobResourceHistory {

    @POST
    @Path("history")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobHistory(            
            @HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, TaskHistoryFilter taskHistoryFilter) throws Exception;
}

