package com.github.angelndevil2.loadt.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 17.
 */
@Data
public class ViewOptions implements Serializable {
    private static final long serialVersionUID = -1206494136826305779L;


    /**
     * view interval in millis, 0 means every sample
     */
    private long viewInterval;
}
