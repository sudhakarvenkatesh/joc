
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * start job commands
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "jobs"
})
public class StartJobs {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    private List<StartJob> jobs = new ArrayList<StartJob>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
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
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public List<StartJob> getJobs() {
        return jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(List<StartJob> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartJobs) == false) {
            return false;
        }
        StartJobs rhs = ((StartJobs) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobs, rhs.jobs).isEquals();
    }

}