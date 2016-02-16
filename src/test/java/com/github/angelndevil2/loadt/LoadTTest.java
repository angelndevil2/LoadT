package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.listener.ConsoleStatisticViewer;
import com.github.angelndevil2.loadt.loadmanager.LoadManagerType;
import com.github.angelndevil2.loadt.util.PropertiesUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author k, Created on 16. 2. 6.
 */
public class LoadTTest {

    @Test
    public void testLoadT() throws LoadTException, IOException, InterruptedException {

        LoadT loadT = new LoadT();

        PropertiesUtil.setDirs("src/dist");

        // set LoadManager
        final String name = "Test load manager";
        loadT.addLoadManager(name, LoadManagerType.JMETER);


        // set options

        // use keepAlive
        loadT.setHttpKeepAlive(name, true);

        // follow redirect
        loadT.setHttpFollowRedirect(name, true);

        // set loop count 10
        loadT.setLoopCount(name,100);

        // set number of thread 100
        loadT.setNumberOfThread(name, 100);

        // add http sampler
        // loadT.addHttpSampler(name, "websphere", "192.168.100.241", 10370, "/ext", HTTPMethod.GET, "192.168.100.241");

        // add system information collector with domain "192.168.100.241"
        //SystemInfoCollector systemInfoCollector = new SystemInfoCollector("192.168.100.241");
        // start system information collecting
        //systemInfoCollector.start();
        //loadT.addSystemInfoCollector(name, systemInfoCollector);

        // add console viewer
        //loadT.addListener(name, new ConsoleResultViewer());
        loadT.addListener(name, new ConsoleStatisticViewer());

        // run test
        loadT.runTestAll();
/*
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }*/
    }
}
