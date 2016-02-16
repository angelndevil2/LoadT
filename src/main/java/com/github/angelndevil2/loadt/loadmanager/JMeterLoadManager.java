package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.util.PropertiesUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

/**
 * JMeter LoadManager.
 *
 * @author k, Created on 16. 2. 5.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper=true)
public class JMeterLoadManager extends LoadManager {

    public JMeterLoadManager(String name) throws LoadTException {
        super(name);
        initJMeter();
        resultCollector = new JMeterResultCollector();
    }
    /**
     * @return LoadManagerType
     */
    public LoadManagerType getType() {
        return LoadManagerType.JMETER;
    }

    /**
     * prepare test
     *
     */
    @Override
    public void prepareTest() throws LoadTException {
        // for error check, call super
        super.prepareTest();

        initThreadGroup();

        // add test plan
        testPlanTree.add(testPlan);

        // add threadGroup under test plan
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);

        // transform HttpSampler to JMeter HttpSamplerProxy
        HTTPOptions httpOptions = getContext().getHttpOptions();
        for (String samplerName : getContext().getHttpSamplers().keySet()) {
            HttpSampler sampler = getContext().getHttpSamplers().get(samplerName);
            HTTPSamplerProxy samplerProxy = new HTTPSamplerProxy();
            samplerProxy.setName(samplerName);
            samplerProxy.setDomain(sampler.getDomain());
            samplerProxy.setPort(sampler.getPort());
            samplerProxy.setPath(sampler.getPath());
            samplerProxy.setMethod(sampler.getMethod().name());
            samplerProxy.setUseKeepAlive(httpOptions.isKeepAlive());
            samplerProxy.setFollowRedirects(httpOptions.isFollowRedirect());
            threadGroupHashTree.add(samplerProxy);
        }

        initResultCollector();
        testPlanTree.add(testPlanTree.getArray()[0], resultCollector);

        engine.configure(testPlanTree);
    }

    /**
     * run test actually
     */
    public void performTest() {
        engine.run();
    }

    /**
     * JMeter initialization (properties, log levels, locale, etc)
     */
    private void initJMeter() {
        JMeterUtils.setJMeterHome(PropertiesUtil.getBaseDir());
        JMeterUtils.loadJMeterProperties(PropertiesUtil.getJMeterPropertiesFile());
        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
        JMeterUtils.initLocale();
    }

    /**
     * init thread group
     */
    private void initThreadGroup() {

        threadGroup.setNumThreads(getContext().getNumberOfThread());
        threadGroup.setRampUp(getContext().getRampUpTime());

        initLoopController();
        threadGroup.setSamplerController(loopController);
        loopController.setContinueForever(getContext().isLoopForever()); // must be after setSamplerController
    }

    /**
     * init loop controller
     */
    private void initLoopController() {
        loopController.initialize();
        loopController.setLoops(getContext().getLoopCount());
        loopController.setFirst(true);
    }

    /**
     * init result collector
     */
    private void initResultCollector() {

        for (IResultListener listener : getContext().getListeners()) {
            try {

                resultCollector.addListener(listener);
                log.debug("listener {} appended.", listener.toString());

            } catch (LoadTException e) {
                log.warn(e.getMessage(), e);
            }
        }

        initCalculator();
    }

    private void initCalculator() {
        for (IResultCalculator calculator : getContext().getCalculators().values()) {
            try {
                resultCollector.addCalculator(calculator);
                calculator.start();
                log.debug("{} started", calculator);
            } catch (LoadTException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    private StandardJMeterEngine engine = new StandardJMeterEngine();
    private final HashTree testPlanTree = new HashTree();
    private final TestPlan testPlan = new TestPlan();
    private final ThreadGroup threadGroup = new ThreadGroup();
    private final LoopController loopController = new LoopController();
    private final JMeterResultCollector resultCollector;
}
