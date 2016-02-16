package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.ResultType;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface IResultListener extends Serializable {

    void sampleOccurred(ISample result);

    ResultType getResultType();
}
