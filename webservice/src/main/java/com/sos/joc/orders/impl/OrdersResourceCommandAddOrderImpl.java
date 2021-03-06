package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.AddedOrders;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.model.order.OrderPath200;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;

@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {

	private static final String API_CALL = "./orders/add";
	private List<Err419> listOfErrors = new ArrayList<Err419>();
	private List<OrderPath200> orderPaths = new ArrayList<OrderPath200>();

	@Override
	public JOCDefaultResponse postOrdersAdd(String xAccessToken, String accessToken, ModifyOrders modifyOrders)
			throws Exception {
		return postOrdersAdd(getAccessToken(xAccessToken, accessToken), modifyOrders);
	}

	public JOCDefaultResponse postOrdersAdd(String accessToken, ModifyOrders modifyOrders) throws Exception {
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyOrders, accessToken,
					modifyOrders.getJobschedulerId(),
					getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isAddOrder());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredComment(modifyOrders.getAuditLog());
			if (modifyOrders.getOrders().size() == 0) {
				throw new JocMissingRequiredParameterException("undefined 'orders'");
			}
			AddedOrders entity = new AddedOrders();
			for (ModifyOrder order : modifyOrders.getOrders()) {
				executeAddOrderCommand(order, modifyOrders);
			}
			entity.setOrders(orderPaths);
			if (orderPaths.isEmpty()) {
				entity.setOrders(null);
			}
			entity.setDeliveryDate(Date.from(Instant.now()));
			if (listOfErrors.size() > 0) {
				entity.setErrors(listOfErrors);
				entity.setOk(null);
				return JOCDefaultResponse.responseStatus419(entity);
			} else {
				entity.setErrors(null);
				entity.setOk(true);
			}
			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

	private void executeAddOrderCommand(ModifyOrder order, ModifyOrders modifyOrders) {

		try {
			if (order.getParams() != null && order.getParams().isEmpty()) {
				order.setParams(null);
			}
			ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
			logAuditMessage(orderAudit);

			checkRequiredParameter("jobChain", order.getJobChain());
			XMLBuilder xml = new XMLBuilder("add_order");
			xml.addAttribute("job_chain", normalizePath(order.getJobChain()));

			if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
				xml.addAttribute("id", normalizePath(order.getOrderId()));
			}
			if ((order.getAt() == null || "".equals(order.getAt()))
					&& (order.getRunTime() == null || "".equals(order.getRunTime()))) {
				xml.addAttribute("at", "now");
			}
			if (order.getAt() != null && !"".equals(order.getAt())) {
				if (order.getAt().contains("now")) {
					xml.addAttribute("at", order.getAt());
				} else {
					xml.addAttribute("at", JobSchedulerDate.getAtInJobSchedulerTimezone(order.getAt(),
							order.getTimeZone(), dbItemInventoryInstance.getTimeZone()));
				}
			}
			if (order.getEndState() != null && !"".equals(order.getEndState())) {
				xml.addAttribute("end_state", order.getEndState());
			}
			if (order.getState() != null && !"".equals(order.getState())) {
				xml.addAttribute("state", order.getState());
			}
			if (order.getTitle() != null) {
				xml.addAttribute("title", order.getTitle());
			}
			if (order.getPriority() != null && order.getPriority() >= 0) {
				xml.addAttribute("priority", order.getPriority().toString());
			}
			if (order.getParams() != null && !order.getParams().isEmpty()) {
				Element params = XMLBuilder.create("params");
				for (NameValuePair param : order.getParams()) {
					params.addElement("param").addAttribute("name", param.getName()).addAttribute("value",
							param.getValue());
				}
				xml.add(params);
			}
			if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
				try {
					ValidateXML.validateRunTimeAgainstJobSchedulerSchema(order.getRunTime());
					xml.add(XMLBuilder.parse(order.getRunTime()));
				} catch (JocException e) {
					throw e;
				} catch (Exception e) {
					throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
				}
			}
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());

			OrderPath200 orderPath = new OrderPath200();
			orderPath.setSurveyDate(jocXmlCommand.getSurveyDate());
			orderPath.setJobChain(order.getJobChain());

			if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
				try {
					String orderId = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/ok/order/@id");
					if (orderId != null && !orderId.isEmpty()) {
						orderAudit.setOrderId(orderId);
					}
					orderPath.setOrderId(orderId);
				} catch (Exception e) {
					orderPath.setOrderId(null);
				}
			}
			storeAuditLogEntry(orderAudit);
			orderPaths.add(orderPath);
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), order));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), order));
		}
	}
}
