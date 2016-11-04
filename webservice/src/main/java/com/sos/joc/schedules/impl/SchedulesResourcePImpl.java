package com.sos.joc.schedules.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.schedule.SchedulePermanent;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.SchedulePath;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesP;
import com.sos.joc.schedules.resource.ISchedulesResourceP;

@Path("schedules")
public class SchedulesResourcePImpl extends JOCResourceImpl implements ISchedulesResourceP {

    private static final String API_CALL = "./schedules/p";
    private String regex;
    private List<Folder> folders;
    private List<SchedulePath> schedules;

    @Override
    public JOCDefaultResponse postSchedulesP(String accessToken, SchedulesFilter schedulesFilter) throws Exception {
        try {
            initLogging(API_CALL, schedulesFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, schedulesFilter.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // FILTER
            folders = schedulesFilter.getFolders();
            schedules = schedulesFilter.getSchedules();
            regex = schedulesFilter.getRegex();

            Globals.beginTransaction();
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(Globals.sosHibernateConnection);
            List<ScheduleP> listOfSchedules = new ArrayList<ScheduleP>();

            if (schedules != null && !schedules.isEmpty()) {
                List<ScheduleP> schedulesToAdd = new ArrayList<ScheduleP>();
                for (SchedulePath schedulePath : schedules) {
                    DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(schedulePath.getSchedule(), dbItemInventoryInstance.getId());
                    ScheduleP scheduleP = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, dbItemInventoryInstance);
                    if (scheduleP != null) {
                        schedulesToAdd.add(scheduleP);
                    }
                }
                if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                    listOfSchedules.addAll(schedulesToAdd);
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventorySchedule> schedulesFromDb = dbLayer.getSchedulesByFolders(folder.getFolder(), dbItemInventoryInstance.getId(),
                            folder.getRecursive().booleanValue());
                    List<ScheduleP> schedulesToAdd = getSchedulesToAdd(dbLayer, schedulesFromDb, dbItemInventoryInstance);
                    if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                        listOfSchedules.addAll(schedulesToAdd);
                    }
                }
            } else {
                List<DBItemInventorySchedule> processClassesFromDb = dbLayer.getSchedules(dbItemInventoryInstance.getId());
                List<ScheduleP> schedulesToAdd = getSchedulesToAdd(dbLayer, processClassesFromDb, dbItemInventoryInstance);
                if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                    listOfSchedules.addAll(schedulesToAdd);
                }
            }
            SchedulesP entity = new SchedulesP();
            entity.setSchedules(listOfSchedules);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }

    private List<ScheduleP> getSchedulesToAdd(InventorySchedulesDBLayer dbLayer, List<DBItemInventorySchedule> schedulesFromDb,
            DBItemInventoryInstance instance) throws Exception {
        List<ScheduleP> schedulesToAdd = new ArrayList<ScheduleP>();
        if (schedulesFromDb != null) {
            for (DBItemInventorySchedule scheduleFromDb : schedulesFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(scheduleFromDb.getName());
                    if (regExMatcher.find()) {
                        schedulesToAdd.add(SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, instance));
                    }
                } else {
                    schedulesToAdd.add(SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, instance));
                }
            }
        }
        return schedulesToAdd;
    }

}