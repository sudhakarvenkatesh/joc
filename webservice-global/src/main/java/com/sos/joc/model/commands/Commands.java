//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.01.10 um 05:11:36 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r Commands complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Commands">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}add_jobs"/>
 *         &lt;element ref="{}add_order"/>
 *         &lt;element name="check_folders" type="{}check_folders"/>
 *         &lt;element ref="{}kill_task"/>
 *         &lt;element ref="{}licence.use"/>
 *         &lt;element name="lock" type="{}lock"/>
 *         &lt;element name="lock.remove" type="{}lock.remove"/>
 *         &lt;element ref="{}modify_job"/>
 *         &lt;element ref="{}modify_hot_folder"/>
 *         &lt;element ref="{}modify_order"/>
 *         &lt;element ref="{}modify_spooler"/>
 *         &lt;element ref="{}job.why"/>
 *         &lt;element ref="{}job_chain.modify"/>
 *         &lt;element ref="{}job_chain_node.modify"/>
 *         &lt;element name="order" type="{}order"/>
 *         &lt;element ref="{}plugin.command"/>
 *         &lt;element name="process_class" type="{}process_class"/>
 *         &lt;element name="process_class.remove" type="{}process_class.remove"/>
 *         &lt;element ref="{}job_chain.check_distributed"/>
 *         &lt;element ref="{}register_remote_scheduler"/>
 *         &lt;element ref="{}remove_job_chain"/>
 *         &lt;element ref="{}remove_order"/>
 *         &lt;element name="run_time" type="{}run_time"/>
 *         &lt;element ref="{}setting.set"/>
 *         &lt;element ref="{}scheduler_log.log_categories.reset"/>
 *         &lt;element ref="{}scheduler_log.log_categories.set"/>
 *         &lt;element ref="{}scheduler_log.log_categories.show"/>
 *         &lt;element ref="{}show_calendar"/>
 *         &lt;element ref="{}show_history"/>
 *         &lt;element ref="{}show_job"/>
 *         &lt;element ref="{}show_jobs"/>
 *         &lt;element ref="{}show_job_chains"/>
 *         &lt;element ref="{}show_job_chain"/>
 *         &lt;element ref="{}show_order"/>
 *         &lt;element ref="{}show_process_classes"/>
 *         &lt;element ref="{}s"/>
 *         &lt;element ref="{}show_schedulers"/>
 *         &lt;element ref="{}show_state"/>
 *         &lt;element ref="{}show_task"/>
 *         &lt;element ref="{}service_request"/>
 *         &lt;element ref="{}start_job"/>
 *         &lt;element ref="{}subsystem.show"/>
 *         &lt;element ref="{}remote_scheduler.start_remote_task"/>
 *         &lt;element ref="{}remote_scheduler.remote_task.close"/>
 *         &lt;element ref="{}remote_scheduler.remote_task.kill"/>
 *         &lt;element ref="{}terminate"/>
 *       &lt;/choice>
 *       &lt;attribute name="on_exit_code">
 *         &lt;simpleType>
 *           &lt;union>
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                 &lt;enumeration value="success"/>
 *                 &lt;enumeration value="error"/>
 *                 &lt;enumeration value="signal"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *             &lt;simpleType>
 *               &lt;list>
 *                 &lt;simpleType>
 *                   &lt;union memberTypes=" {http://www.w3.org/2001/XMLSchema}integer {}Unix_signal_name">
 *                   &lt;/union>
 *                 &lt;/simpleType>
 *               &lt;/list>
 *             &lt;/simpleType>
 *           &lt;/union>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Commands", propOrder = {
    "addJobsOrAddOrderOrCheckFolders"
})
public class Commands {

    @XmlElementRefs({
        @XmlElementRef(name = "remote_scheduler.start_remote_task", type = RemoteSchedulerStartRemoteTask.class, required = false),
        @XmlElementRef(name = "lock.remove", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "remote_scheduler.remote_task.kill", type = RemoteSchedulerRemoteTaskKill.class, required = false),
        @XmlElementRef(name = "modify_order", type = ModifyOrder.class, required = false),
        @XmlElementRef(name = "remove_order", type = RemoveOrder.class, required = false),
        @XmlElementRef(name = "show_calendar", type = ShowCalendar.class, required = false),
        @XmlElementRef(name = "show_order", type = ShowOrder.class, required = false),
        @XmlElementRef(name = "subsystem.show", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.reset", type = SchedulerLogLogCategoriesReset.class, required = false),
        @XmlElementRef(name = "show_job", type = ShowJob.class, required = false),
        @XmlElementRef(name = "terminate", type = Terminate.class, required = false),
        @XmlElementRef(name = "register_remote_scheduler", type = RegisterRemoteScheduler.class, required = false),
        @XmlElementRef(name = "process_class", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "order", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "job.why", type = JobWhy.class, required = false),
        @XmlElementRef(name = "show_schedulers", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "add_jobs", type = AddJobs.class, required = false),
        @XmlElementRef(name = "show_job_chains", type = ShowJobChains.class, required = false),
        @XmlElementRef(name = "modify_spooler", type = ModifySpooler.class, required = false),
        @XmlElementRef(name = "show_process_classes", type = ShowProcessClasses.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.set", type = SchedulerLogLogCategoriesSet.class, required = false),
        @XmlElementRef(name = "check_folders", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_job_chain", type = ShowJobChain.class, required = false),
        @XmlElementRef(name = "job_chain.check_distributed", type = JobChainCheckDistributed.class, required = false),
        @XmlElementRef(name = "start_job", type = StartJob.class, required = false),
        @XmlElementRef(name = "process_class.remove", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "remove_job_chain", type = RemoveJobChain.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.show", type = SchedulerLogLogCategoriesShow.class, required = false),
        @XmlElementRef(name = "licence.use", type = LicenceUse.class, required = false),
        @XmlElementRef(name = "job_chain_node.modify", type = JobChainNodeModify.class, required = false),
        @XmlElementRef(name = "setting.set", type = SettingSet.class, required = false),
        @XmlElementRef(name = "show_task", type = ShowTask.class, required = false),
        @XmlElementRef(name = "kill_task", type = KillTask.class, required = false),
        @XmlElementRef(name = "show_history", type = ShowHistory.class, required = false),
        @XmlElementRef(name = "modify_hot_folder", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "add_order", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "run_time", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_jobs", type = ShowJobs.class, required = false),
        @XmlElementRef(name = "remote_scheduler.remote_task.close", type = RemoteSchedulerRemoteTaskClose.class, required = false),
        @XmlElementRef(name = "show_state", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "job_chain.modify", type = JobChainModify.class, required = false),
        @XmlElementRef(name = "s", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "lock", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "plugin.command", type = PluginCommand.class, required = false),
        @XmlElementRef(name = "service_request", type = ServiceRequest.class, required = false),
        @XmlElementRef(name = "modify_job", type = ModifyJob.class, required = false)
    })
    protected List<Object> addJobsOrAddOrderOrCheckFolders;
    @XmlAttribute(name = "on_exit_code")
    protected List<String> onExitCode;

    /**
     * Gets the value of the addJobsOrAddOrderOrCheckFolders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addJobsOrAddOrderOrCheckFolders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddJobsOrAddOrderOrCheckFolders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteSchedulerStartRemoteTask }
     * {@link JAXBElement }{@code <}{@link LockRemove }{@code >}
     * {@link RemoteSchedulerRemoteTaskKill }
     * {@link ModifyOrder }
     * {@link RemoveOrder }
     * {@link ShowCalendar }
     * {@link ShowOrder }
     * {@link JAXBElement }{@code <}{@link SubsystemShow }{@code >}
     * {@link SchedulerLogLogCategoriesReset }
     * {@link ShowJob }
     * {@link Terminate }
     * {@link RegisterRemoteScheduler }
     * {@link JAXBElement }{@code <}{@link ProcessClass }{@code >}
     * {@link JAXBElement }{@code <}{@link Order }{@code >}
     * {@link JobWhy }
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link AddJobs }
     * {@link ShowJobChains }
     * {@link ModifySpooler }
     * {@link ShowProcessClasses }
     * {@link SchedulerLogLogCategoriesSet }
     * {@link JAXBElement }{@code <}{@link CheckFolders }{@code >}
     * {@link ShowJobChain }
     * {@link JobChainCheckDistributed }
     * {@link StartJob }
     * {@link JAXBElement }{@code <}{@link ProcessClassRemove }{@code >}
     * {@link RemoveJobChain }
     * {@link SchedulerLogLogCategoriesShow }
     * {@link LicenceUse }
     * {@link JobChainNodeModify }
     * {@link SettingSet }
     * {@link ShowTask }
     * {@link KillTask }
     * {@link ShowHistory }
     * {@link JAXBElement }{@code <}{@link ModifyHotFolder }{@code >}
     * {@link JAXBElement }{@code <}{@link Order }{@code >}
     * {@link JAXBElement }{@code <}{@link RunTime }{@code >}
     * {@link ShowJobs }
     * {@link RemoteSchedulerRemoteTaskClose }
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link JobChainModify }
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link JAXBElement }{@code <}{@link Lock }{@code >}
     * {@link PluginCommand }
     * {@link ServiceRequest }
     * {@link ModifyJob }
     * 
     * 
     */
    public List<Object> getAddJobsOrAddOrderOrCheckFolders() {
        if (addJobsOrAddOrderOrCheckFolders == null) {
            addJobsOrAddOrderOrCheckFolders = new ArrayList<Object>();
        }
        return this.addJobsOrAddOrderOrCheckFolders;
    }

    /**
     * Gets the value of the onExitCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the onExitCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOnExitCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOnExitCode() {
        if (onExitCode == null) {
            onExitCode = new ArrayList<String>();
        }
        return this.onExitCode;
    }

}
