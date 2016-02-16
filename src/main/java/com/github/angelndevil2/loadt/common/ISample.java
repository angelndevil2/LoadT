package com.github.angelndevil2.loadt.common;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface ISample extends Serializable {

    /**
     *
     * @return sample to printable string
     */
    String toPrintableString();

    /**
     *
     * @return sampler name
     */
    String getLabel();

    Double getCpuBusy();
}
