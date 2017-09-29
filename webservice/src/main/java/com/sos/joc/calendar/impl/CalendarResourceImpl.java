package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendar200;
import com.sos.joc.model.calendar.CalendarFilter;

@Path("calendar")
public class CalendarResourceImpl extends JOCResourceImpl implements ICalendarResource {

    private static final String API_CALL = "./calendar";

    @Override
    public JOCDefaultResponse postCalendar(String accessToken, CalendarFilter calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(
                    accessToken).getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("calendar path", calendarFilter.getPath());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            String calendarPath = normalizePath(calendarFilter.getPath());
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            DBItemCalendar calendarItem = dbLayer.getCalendar(calendarPath);
            if (calendarItem == null) {
                throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
            }
            Calendar calendar = new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class);
            calendar.setPath(calendarPath);
            calendar.setName(calendarItem.getBaseName());
            Calendar200 entity = new Calendar200();
            entity.setCalendar(calendar);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }
    
}