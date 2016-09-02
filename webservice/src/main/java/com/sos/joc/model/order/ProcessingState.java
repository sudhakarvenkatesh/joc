
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public enum ProcessingState {

    PENDING("PENDING"),
    RUNNING("RUNNING"),
    WAITINGFORRESOURCE("WAITINGFORRESOURCE"),
    SUSPENDED("SUSPENDED"),
    SETBACK("SETBACK"),
    BLACKLIST("BLACKLIST");
    private final String value;
    private final static Map<String, ProcessingState> CONSTANTS = new HashMap<String, ProcessingState>();

    static {
        for (ProcessingState c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ProcessingState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static ProcessingState fromValue(String value) {
        ProcessingState constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
