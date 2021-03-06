package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

public class CalendarUsageDBLayer extends DBLayer {

    private static final String CALENDAR_USAGES_INSTANCE = CalendarUsagesAndInstance.class.getName();
    private Query<DBItemInventoryCalendarUsage> query;

    public CalendarUsageDBLayer(SOSHibernateSession connection) {
        super(connection);
    }

    private String getWhere(CalendarUsageFilter filter) {
        String where = "";
        String and = "";
        if (filter.getObjectType() != null && !filter.getObjectType().isEmpty()) {
            where += and + " objectType = :objectType";
            and = " and ";
        }

        if (filter.getInstanceId() != null) {
            where += and + " instanceId = :instanceId";
            and = " and ";
        }

        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            where += and + " path = :path";
            and = " and ";
        }

        if (filter.getCalendarId() != null) {
            where += and + " calendarId = :calendarId";
            and = " and ";
        }
        if (!where.trim().isEmpty()) {
            where = " where " + where;
        }
        return where;
    }

    public void saveCalendarUsage(DBItemInventoryCalendarUsage calendarUsageDbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (calendarUsageDbItem != null) {
                calendarUsageDbItem.setCreated(new Date());
                calendarUsageDbItem.setModified(new Date());
                getSession().save(calendarUsageDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void updateCalendarUsage(DBItemInventoryCalendarUsage calendarUsageDbItem) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (calendarUsageDbItem != null) {
                calendarUsageDbItem.setModified(new Date());
                getSession().update(calendarUsageDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    private void bindParameters(CalendarUsageFilter filter) {
        if (filter.getCalendarId() != null) {
            query.setParameter("calendarId", filter.getCalendarId());
        }
        if (filter.getInstanceId() != null) {
            query.setParameter("instanceId", filter.getInstanceId());
        }
        if (filter.getObjectType() != null && !filter.getObjectType().isEmpty()) {
            query.setParameter("objectType", filter.getObjectType());
        }
        if (filter.getPath() != null && !filter.getPath().isEmpty()) {
            query.setParameter("path", filter.getPath());
        }
    }

    public int deleteCalendarUsage(CalendarUsageFilter calendarUsageFilter) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            String hql = "delete from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(calendarUsageFilter);
            int row = 0;
            query = getSession().createQuery(hql);
            bindParameters(calendarUsageFilter);
            row = getSession().executeUpdate(query);
            return row;

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public int deleteCalendarUsage(Long calendarId) throws DBConnectionRefusedException, DBInvalidDataException {
        CalendarUsageFilter calendarUsageFilter = new CalendarUsageFilter();
        calendarUsageFilter.setCalendarId(calendarId);
        int row = deleteCalendarUsage(calendarUsageFilter);
        return row;
    }

    public void deleteCalendarUsage(DBItemInventoryCalendarUsage calendarUsage) throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            getSession().delete(calendarUsage);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public void updateEditFlag(Set<DBItemInventoryCalendarUsage> calendarUsages, boolean update) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            if (calendarUsages != null) {
                for (DBItemInventoryCalendarUsage item : calendarUsages) {
                    if (update) {
                        if (item.getEdited() == null) {
                            getSession().delete(item);
                        } else {
                            item.setModified(new Date());
                            getSession().update(item);
                        }
                    } else {
                        if (item.getEdited() != null && item.getEdited()) {
                            item.setModified(new Date());
                            getSession().update(item);
                        } else {
                            getSession().delete(item);
                        }
                    }
                }
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryCalendarUsage> getCalendarUsagesOfAnObject(Long instanceId, String objectType, String path)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setInstanceId(instanceId);
            filter.setObjectType(objectType);
            filter.setPath(path);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    public DBItemInventoryCalendarUsage getCalendarUsageOfAnObject(Long instanceId, String calendarPath, String objectType, String objectPath)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select icu from ").append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CALENDARS).append(" ic ");
            sql.append(" where ic.id = icu.calendarId");
            sql.append(" and ic.name = :calendarPath");
            sql.append(" and icu.instanceId = :instanceId");
            sql.append(" and icu.objectType = :objectType");
            sql.append(" and icu.path = :path");
            query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", objectPath);
            query.setParameter("calendarPath", calendarPath);
            return getSession().getSingleResult(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<String> getWorkingDaysCalendarUsagesOfAnObject(Long instanceId, String objectType, String path) throws DBConnectionRefusedException,
            DBInvalidDataException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select icu.configuration from ").append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu, ");
            sql.append(DBITEM_CALENDARS).append(" ic ");
            sql.append(" where ic.id = icu.calendarId");
            sql.append(" and ic.type = 'WORKING_DAYS'");
            sql.append(" and icu.instanceId = :instanceId");
            sql.append(" and icu.objectType = :objectType");
            sql.append(" and icu.path = :path");
            sql.append(" and icu.configuration is not null");
            Query<String> query = getSession().createQuery(sql.toString());
            query.setParameter("instanceId", instanceId);
            query.setParameter("objectType", objectType);
            query.setParameter("path", path);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryCalendarUsage> getCalendarUsages(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setCalendarId(calendarId);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<DBItemInventoryCalendarUsage> getCalendarUsages(Long instanceId, String calendarPath) throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setInstanceId(instanceId);
            filter.setPath(calendarPath);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getResultList(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public DBItemInventoryCalendarUsage getCalendarUsageByConstraint(Long instanceId, Long calendarId, String objectType, String calendarPath)
            throws DBInvalidDataException,
            DBConnectionRefusedException {
        try {
            CalendarUsageFilter filter = new CalendarUsageFilter();
            filter.setInstanceId(instanceId);
            filter.setCalendarId(calendarId);
            filter.setObjectType(objectType);
            filter.setPath(calendarPath);
            String sql = "from " + DBITEM_INVENTORY_CALENDAR_USAGE + getWhere(filter);
            query = getSession().createQuery(sql);
            bindParameters(filter);
            return getSession().getSingleResult(query);

        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    public List<CalendarUsagesAndInstance> getInstancesFromCalendar(Long calendarId) throws DBInvalidDataException, DBConnectionRefusedException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select new ").append(CALENDAR_USAGES_INSTANCE).append(" (ii) from ");
            sql.append(DBITEM_INVENTORY_INSTANCES).append(" ii, ");
            sql.append(DBITEM_INVENTORY_CALENDAR_USAGE).append(" icu ");
            sql.append("where ii.id = icu.instanceId ");
            if (calendarId != null) {
                sql.append("and icu.calendarId = :calendarId ");
            }
            sql.append("group by ii.id");
            Query<CalendarUsagesAndInstance> query = getSession().createQuery(sql.toString());
            if (calendarId != null) {
                query.setParameter("calendarId", calendarId);
            }
            return getSession().getResultList(query);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
}
