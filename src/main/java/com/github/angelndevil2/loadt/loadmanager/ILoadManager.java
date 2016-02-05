package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.LoadManagerType;

/**
 * @author k, Created on 16. 2. 5.
 */
public interface ILoadManager {

    /**
     *
     * @return LoadManagerType
     */
    LoadManagerType getType();

    /**
     * prepare test
     */
    void prepareTest();

    /**
     * run test actually
     */
    void performTest();
}
