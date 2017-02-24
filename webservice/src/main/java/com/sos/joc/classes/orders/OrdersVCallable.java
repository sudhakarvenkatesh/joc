package com.sos.joc.classes.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderStateFilter;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersFilter;

public class OrdersVCallable implements Callable<Map<String,OrderVolatile>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersVCallable.class);
    private final String job;
    private final OrdersPerJobChain orders;
    private final Folder folder;
    private final OrdersFilter ordersBody;
    private final Boolean compact;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;
    private final Boolean suppressJobSchedulerObjectNotExistException;
    
    public OrdersVCallable(String job, Boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = null;
        this.job = ("/"+job.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
    }
    
    public OrdersVCallable(OrdersPerJobChain orders, Boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = orders;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
    }
    
    public OrdersVCallable(JobChainV jobChain, Boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(jobChain.getPath());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
    }
    
    public OrdersVCallable(OrderFilter order, JOCJsonCommand jocJsonCommand, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(order.getJobChain());
        o.addOrder(order.getOrderId());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = order.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = order.getSuppressNotExistException();
    }
    
    public OrdersVCallable(Folder folder, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = null;
        this.job = null;
        this.folder = folder;
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
    }
    
    @Override
    public Map<String,OrderVolatile> call() throws Exception {
        try {
            if(orders != null) {
                return getOrders(orders, compact, jocJsonCommand, accessToken);
            } else if(job != null) { 
                return getOrders(job, compact, jocJsonCommand, accessToken);
            } else {
                return getOrders(folder, ordersBody, jocJsonCommand, accessToken);
            }
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return new HashMap<String,OrderVolatile>();
            }
            throw e;
        }
    }
    
    public OrderV getOrder() throws Exception {
        Map<String, OrderVolatile> orderMap;
        OrderV o = new OrderV();
        o.setParams(null);
        try {
            orderMap = getOrders(orders, compact, jocJsonCommand, accessToken);
            if (orderMap == null || orderMap.isEmpty()) {
                return o;
            }
            return orderMap.values().iterator().next();
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return o;
            }
            throw e;
        }
    }
    
    public List<OrderV> getOrdersOfJob() throws Exception {
        Map<String,OrderVolatile> orderMap = getOrders(job, compact, jocJsonCommand, accessToken);
        if (orderMap == null || orderMap.isEmpty()) {
            return null;
        }
        return new ArrayList<OrderV>(orderMap.values());
    }
    
    private Map<String,OrderVolatile> getOrders(OrdersPerJobChain orders, boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) throws Exception {
        return getOrders(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(orders), accessToken), orders.getJobChain(), compact, null, null);
    }
    
    private Map<String, OrderVolatile> getOrders(String job, boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) throws Exception {
        return getOrders(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(job), accessToken), null, compact, null, null);
    }

    private Map<String, OrderVolatile> getOrders(Folder folder, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken)
            throws Exception {
        return getOrders(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(folder, ordersBody), accessToken), null, ordersBody
                .getCompact(), ordersBody.getRegex(), ordersBody.getProcessingStates());
    }
    
    private Map<String,OrderVolatile> getOrders(JsonObject json, String origJobChain, boolean compact, String regex, List<OrderStateFilter> processingStates) throws JobSchedulerInvalidResponseDataException {
        UsedNodes usedNodes = new UsedNodes();
        UsedJobs usedJobs = new UsedJobs();
        UsedJobChains usedJobChains = new UsedJobChains();
        UsedTasks usedTasks = new UsedTasks();
        usedNodes.addEntries(json.getJsonArray("usedNodes"));
        usedTasks.addEntries(json.getJsonArray("usedTasks"));
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        Map<String,OrderVolatile> listOrderQueue = new HashMap<String,OrderVolatile>();
        
        for (JsonObject ordersItem: json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            OrderVolatile order = new OrderVolatile(ordersItem, origJobChain);
            order.setPathJobChainAndOrderId();
            if (!FilterAfterResponse.matchRegex(regex, order.getPath())) {
                LOGGER.debug("...processing skipped caused by 'regex=" + regex + "'");
                continue; 
            }
            order.setSurveyDate(surveyDate);
            order.setFields(usedNodes, usedTasks, compact);
            if (!order.processingStateIsSet() && order.isWaitingForJob()) {
                usedJobs.addEntries(json.getJsonArray("usedJobs"));
                order.readJobObstacles(usedJobs.get(order.getJob()));
            }
            if (!order.processingStateIsSet()) {
                usedJobChains.addEntries(json.getJsonArray("usedJobChains"));
                order.readJobChainObstacles(usedJobChains.get(order.getJobChain()));
            }
            if (checkSuspendedOrdersWithFilter(order.getProcessingFilterState(), processingStates)) {
                listOrderQueue.put(order.getPath(), order); 
            }
        }
        return listOrderQueue;
    }
    
    private boolean checkSuspendedOrdersWithFilter(OrderStateFilter processingState, List<OrderStateFilter> processingStates) {
        if (processingState == null || processingStates == null) {
            return true;
        }
        return FilterAfterResponse.filterStateHasState(processingStates, processingState);
    }

    private String getServiceBody(OrdersPerJobChain orders) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String jobChain = orders.getJobChain();
        jobChain = ("/"+jobChain.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        builder.add("path", jobChain);
        if (orders.getOrders().size() > 0) {
            JsonArrayBuilder ordersArray = Json.createArrayBuilder();
            for (String order : orders.getOrders()) {
                ordersArray.add(order);
            } 
            builder.add("orderIds", ordersArray);
        }
        return builder.build().toString();
    }
    
    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder oBuilder = Json.createObjectBuilder();
        JsonArrayBuilder aBuilder =  Json.createArrayBuilder();
        aBuilder.add(job);
        oBuilder.add("jobPaths", aBuilder);
        return oBuilder.build().toString();
    }
    
    private String getServiceBody(Folder folder, OrdersFilter ordersBody) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        // add folder path from response body 
        String path = folder.getFolder();
        if (path != null && !path.isEmpty()) {
            path = ("/"+path.trim()+"/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
                LOGGER.debug(String.format("...consider 'recursive=%1$b' for folder '%2$s'", folder.getRecursive(), folder.getFolder()));
            }
            builder.add("path", path);
        }
        
        // add processingState from response body
        List<OrderStateFilter> states = ordersBody.getProcessingStates();
        Map<String, Boolean> filterValues = new HashMap<String, Boolean>();
        boolean suspended = false;
        if (states != null && !states.isEmpty()) {
            for (OrderStateFilter state : states) {
                switch (state) {
                case PENDING:
                    filterValues.put("Planned", true);
                    filterValues.put("NotPlanned", true);
                    break;
                case RUNNING:
                    filterValues.put("InTaskProcess", true);
                    filterValues.put("OccupiedByClusterMember", true);
                    break;
                case SUSPENDED:
                    suspended = true;
                    break;
                case BLACKLIST:
                    filterValues.put("Blacklisted", true);
                    break;
                case SETBACK:
                    filterValues.put("Setback", true);
                    break;
                default:
                    filterValues.put("Due", true);
                    filterValues.put("WaitingInTask", true);
                    filterValues.put("WaitingForResource", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            JsonArrayBuilder processingStates = Json.createArrayBuilder();
            for (String key : filterValues.keySet()) {
                processingStates.add(key);
            }
            builder.add("isOrderProcessingState", processingStates);
            if (suspended) {
                builder.add("orIsSuspended",true); 
            }
        } else {
            if (suspended) {
                builder.add("isSuspended",true); 
            }
        }
        
        // add type from response body
        List<OrderType> types = ordersBody.getTypes();
        filterValues.clear();
        if (types != null && !types.isEmpty()) {
            for (OrderType type : types) {
                switch (type) {
                case AD_HOC:
                    filterValues.put("AdHoc", true);
                    break;
                case PERMANENT:
                    filterValues.put("Permanent", true);
                    break;
                case FILE_ORDER:
                    filterValues.put("FileOrder", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            JsonArrayBuilder sourceTypes = Json.createArrayBuilder();
            for (String key : filterValues.keySet()) {
                sourceTypes.add(key);
            }
            builder.add("isOrderSourceType", sourceTypes);
        }
        
        return builder.build().toString();
    }
}
