package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.LoadManagerType;

/**
 * Dummy LoadManager for module testing.
 *
 * @author k, Created on 16. 2. 5.
 */
public class TestLoadManager implements ILoadManager {
    /**
     * @return LoadManagerType
     */
    @Override
    public LoadManagerType getType() {
        return LoadManagerType.DUMMY;
    }

    /**
     * prepare test
     */
    @Override
    public void prepareTest() {

    }

    /**
     * run test actually
     */
    @Override
    public void performTest() {

    }
}
