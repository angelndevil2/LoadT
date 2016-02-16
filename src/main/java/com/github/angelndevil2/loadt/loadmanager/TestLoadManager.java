package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.LoadTException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * Dummy LoadManager for module testing.
 *
 * @author k, Created on 16. 2. 5.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper=true)
public class TestLoadManager extends LoadManager {

    public TestLoadManager(String name) throws LoadTException {
        super(name);
    }
    /**
     * @return LoadManagerType
     */
    public LoadManagerType getType() {
        return LoadManagerType.DUMMY;
    }

    /**
     * prepare test
     */
    @Override
    public void prepareTest() throws LoadTException {
        // for error check, call super
        super.prepareTest();
        System.out.println("prepared test.");
    }

    /**
     * run test actually
     */
    public void performTest() {
        System.out.println("start test.");
    }

    public boolean isActive() {
        return false;
    }
}
