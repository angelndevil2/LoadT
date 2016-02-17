package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.HTTPMethod;
import com.github.angelndevil2.loadt.common.JMeterCalculator;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.SystemInfoCollector;
import com.github.angelndevil2.loadt.listener.CSVFileSaver;
import com.github.angelndevil2.loadt.listener.ConsoleStatisticViewer;
import com.github.angelndevil2.loadt.listener.IResultSaver;
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
        loadT.setLoopCount(name,1000);

        // set number of thread 100
        loadT.setNumberOfThread(name, 1);

       // add http sampler
        loadT.addHttpSampler(name, "websphere", "192.168.100.241", 10370, "/ext", HTTPMethod.GET, "192.168.100.241");

        // add system information collector with domain "192.168.100.241"
        SystemInfoCollector systemInfoCollector = new SystemInfoCollector("192.168.100.241");
        loadT.addSystemInfoCollector(systemInfoCollector);
        loadT.startSystemInfoCollectors();

        // add console viewer
        //loadT.addListener(name, new ConsoleResultViewer());
        loadT.addCalculator(name, new JMeterCalculator("TOTAL"));
        loadT.addStatisticSampleListener(name, "TOTAL", new ConsoleStatisticViewer());

        loadT.setSaveInterval(1000);
        IResultSaver saver = new CSVFileSaver("test.csv");
        loadT.addResultSaver(name, saver);
        // result saver is also IResultListener, so register with addStatisticSampleListener or addListener.
        loadT.addStatisticSampleListener(name, "TOTAL", saver);

        // run test
        loadT.runTestAll();
    }
}
