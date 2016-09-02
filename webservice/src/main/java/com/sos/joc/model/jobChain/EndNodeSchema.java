
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain end node without a job or a file sink
 * <p>
 * job chain end node of a file sink with 'remove' or 'move' which are exclusivly
 * 
 */
@Generated("org.jsonschema2pojo")
public class EndNodeSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String name;
    private Boolean remove;
    /**
     * a directory path is expected
     * 
     */
    private String move;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The remove
     */
    public Boolean getRemove() {
        return remove;
    }

    /**
     * 
     * @param remove
     *     The remove
     */
    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    /**
     * a directory path is expected
     * 
     * @return
     *     The move
     */
    public String getMove() {
        return move;
    }

    /**
     * a directory path is expected
     * 
     * @param move
     *     The move
     */
    public void setMove(String move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(remove).append(move).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EndNodeSchema) == false) {
            return false;
        }
        EndNodeSchema rhs = ((EndNodeSchema) other);
        return new EqualsBuilder().append(name, rhs.name).append(remove, rhs.remove).append(move, rhs.move).isEquals();
    }

}
