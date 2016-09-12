
package com.sos.joc.model.common;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobScheduler filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobSchedulerFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerFilterSchema) == false) {
            return false;
        }
        JobSchedulerFilterSchema rhs = ((JobSchedulerFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).isEquals();
    }

}