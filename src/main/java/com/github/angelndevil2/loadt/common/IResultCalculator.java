package com.github.angelndevil2.loadt.common;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface IResultCalculator extends Serializable {

    StatisticSample calcSample(ISample sample);
}
