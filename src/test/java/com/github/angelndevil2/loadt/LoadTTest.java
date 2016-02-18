package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.HTTPMethod;
import com.github.angelndevil2.loadt.common.JMeterCalculator;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.SystemInfoCollector;
import com.github.angelndevil2.loadt.jetty.JettyServer;
import com.github.angelndevil2.loadt.jetty.PropList;
import com.github.angelndevil2.loadt.listener.CSVFileSaver;
import com.github.angelndevil2.loadt.listener.ConsoleResultViewer;
import com.github.angelndevil2.loadt.listener.ConsoleStatisticViewer;
import com.github.angelndevil2.loadt.listener.IResultSaver;
import com.github.angelndevil2.loadt.loadmanager.LoadManagerType;
import com.github.angelndevil2.loadt.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author k, Created on 16. 2. 6.
 */
@Slf4j
public class LoadTTest {

    static final Properties jettyProp;
    static {
        try {
            PropertiesUtil.setDirs("src/dist");
        } catch (IOException e) {
            log.error("base directory setting error.", e);
        }

        jettyProp = new Properties();

        try {
            jettyProp.load(new FileInputStream(PropertiesUtil.getJettyPropertiesFile()));
        } catch (IOException e) {
            log.error("error loading jetty properties.", e);
        }
    }

    @Test
    public void testLoadT() throws LoadTException, IOException, InterruptedException {

        LoadT loadT = new LoadT();

        Thread jetty = new Thread(new JettyServer());
        jetty.setDaemon(true);
        jetty.start();

        // set LoadManager
        final String name = "Test load manager";
        loadT.addLoadManager(name, LoadManagerType.JMETER);


        // set options

        // use keepAlive
        loadT.setHttpKeepAlive(name, true);

        // follow redirect
        loadT.setHttpFollowRedirect(name, true);

        // set loop count 10
        loadT.setLoopCount(name,1);

        // set number of thread 100
        loadT.setNumberOfThread(name, 1);

       // add http sampler
        loadT.addHttpSampler(name, "websphere", "localhost", Integer.valueOf((String)jettyProp.get(PropList.HTTP_PORT)), "/", HTTPMethod.GET, "localhost");

        // add system information collector with domain "192.168.100.241"
        SystemInfoCollector systemInfoCollector = new SystemInfoCollector("localhost");
        loadT.addSystemInfoCollector(systemInfoCollector);
        loadT.startSystemInfoCollectors();

        // add console viewer
        loadT.addListener(name, new ConsoleResultViewer());
        loadT.setViewInterval(1000);
        loadT.addCalculator(name, new JMeterCalculator("TOTAL"));
        loadT.addStatisticSampleListener(name, "TOTAL", new ConsoleStatisticViewer());

        loadT.setSaveInterval(1000);
        IResultSaver saver = new CSVFileSaver("test.csv");
        loadT.addResultSaver(name, saver);
        // result saver is also IResultListener, so register with addStatisticSampleListener or addListener.
        loadT.addStatisticSampleListener(name, "TOTAL", saver);

        // run test
        loadT.runTestAll();
        System.exit(0);
    }
}
