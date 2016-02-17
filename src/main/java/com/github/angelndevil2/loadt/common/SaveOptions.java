package com.github.angelndevil2.loadt.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 5.
 */
@Data
public class SaveOptions implements Serializable {

    private static final long serialVersionUID = 8161690927997541473L;

    /**
     * save interval in millis, 0 means every sample
     */
    private long saveInterval;
}
