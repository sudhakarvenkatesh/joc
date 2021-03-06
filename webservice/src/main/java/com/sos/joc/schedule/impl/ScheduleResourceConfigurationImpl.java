package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.Configuration200;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.schedule.ScheduleConfigurationFilter;
import com.sos.joc.schedule.resource.IScheduleResourceConfiguration;

@Path("schedule")
public class ScheduleResourceConfigurationImpl extends JOCResourceImpl implements IScheduleResourceConfiguration {

	private static final String API_CALL = "./schedule/configuration";

	@Override
	public JOCDefaultResponse postScheduleConfiguration(String xAccessToken, String accessToken,
			ScheduleConfigurationFilter scheduleBody) throws Exception {
		return postScheduleConfiguration(getAccessToken(xAccessToken, accessToken), scheduleBody);
	}

	public JOCDefaultResponse postScheduleConfiguration(String accessToken, ScheduleConfigurationFilter scheduleBody)
			throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, scheduleBody, accessToken,
					scheduleBody.getJobschedulerId(),
					getPermissonsJocCockpit(scheduleBody.getJobschedulerId(), accessToken).getSchedule().getView()
							.isConfiguration());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			checkRequiredParameter("schedule", scheduleBody.getSchedule());

			Configuration200 entity = new Configuration200();
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
			String schedulePath = normalizePath(scheduleBody.getSchedule());
			String scheduleParent = getParent(schedulePath);
			boolean responseInHtml = scheduleBody.getMime() == ConfigurationMime.HTML;
			String xPath = String.format("/spooler/answer//schedules/schedule[@path='%s']", schedulePath);
			String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders source",
					scheduleParent);
			entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "schedule",
					responseInHtml, accessToken));
			entity.setDeliveryDate(Date.from(Instant.now()));

			if (!responseInHtml) {
				connection = Globals.createSosHibernateStatelessConnection(API_CALL);
				CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
				List<DBItemInventoryCalendarUsage> dbCalendars = calendarUsageDBLayer
						.getCalendarUsagesOfAnObject(dbItemInventoryInstance.getId(), "SCHEDULE", schedulePath);
				if (dbCalendars != null && !dbCalendars.isEmpty()) {
					List<Calendar> calendars = new ArrayList<Calendar>();
					ObjectMapper objMapper = new ObjectMapper();
					for (DBItemInventoryCalendarUsage dbCalendar : dbCalendars) {
						if (dbCalendar.getConfiguration() != null && !dbCalendar.getConfiguration().isEmpty()) {
							calendars.add(objMapper.readValue(dbCalendar.getConfiguration(), Calendar.class));
						}
					}
					entity.setCalendars(calendars);
				}
			}

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