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
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
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

    static LoadT loadT = new LoadT();
    // set LoadManager
    final static String name = "Test load manager";
    static {
        try {
            loadT.addLoadManager(name, LoadManagerType.JMETER);
        } catch (LoadTException e) {
            log.error("{} already exits", name, e);
        }
    }

    static {
        new JettyServer().run();
    }

    @Test
    public void testLoadT() throws LoadTException, IOException, InterruptedException {

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
        loadT.addHttpSampler(name, "test", "localhost", Integer.valueOf((String)jettyProp.get(PropList.HTTP_PORT)), "/", HTTPMethod.GET, "localhost");

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
    }

    @Test
    public void getLoadTInfoFromEmbeddedTest() throws Exception {

        HttpClient client = new HttpClient();
        client.start();

        ContentExchange exchange = new ContentExchange(true);
        exchange.setURL("http://localhost:1080/LoadT/info");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED)
            System.out.println(exchange.getResponseContent());

        exchange.reset();
        exchange.setURL("http://localhost:1080/LoadT/load-managers");


        client.send(exchange);

        // Waits until the exchange is terminated
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED)
            System.out.println(exchange.getResponseContent());

        /*else if (exchangeState == HttpExchange.STATUS_EXCEPTED)
            handleError();
        else if (exchangeState == HttpExchange.STATUS_EXPIRED)
            handleSlowServer();*/
    }
}
