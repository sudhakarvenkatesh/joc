
package com.sos.joc.model.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task history state
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "_text"
})
public class TaskHistoryState {

    /**
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private TaskHistoryStateText _text;

    /**
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public TaskHistoryStateText get_text() {
        return _text;
    }

    /**
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(TaskHistoryStateText _text) {
        this._text = _text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(_text).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskHistoryState) == false) {
            return false;
        }
        TaskHistoryState rhs = ((TaskHistoryState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
