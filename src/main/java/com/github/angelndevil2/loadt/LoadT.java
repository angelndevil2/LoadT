package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.listener.IResultSaver;
import com.github.angelndevil2.loadt.loadmanager.*;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Set options and prepare other stuff for {@link ILoadManager}
 *
 * @author k, Created on 16. 2. 5.
 */
@Data
@Slf4j
public class LoadT {

    private ConcurrentHashMap<String,ILoadManager> loadManagers = new ConcurrentHashMap<String, ILoadManager>();

    /**
     * Global context for every {@link ILoadManager}
     */
    @Getter
    private static final LoadTContext context = new LoadTContext();

    /**
     * register this to {@link ContextUtil}
     */
    public LoadT() {
        ContextUtil.setLoadT(this);
    }

    /**
     *
     * after check if key(LoadManager's name) is exist, add {@link ILoadManager}.<br />
     * if key is already exist, throw {@link LoadTException}
     *
     * @param name load manager's name
     * @param keepAlive true if use http keepalive
     * @throws LoadTException
     */
    public void setHttpKeepAlive(@NonNull String name, boolean keepAlive) throws LoadTException {

        ILoadManager manager = loadManagers.get(name);
        if (manager == null) {
            throw new LoadTException("LoadManager "+name+" is not exist.");
        }
        manager.setHttpKeepAlive(keepAlive);
    }

    /**
     *
     * after check if key(LoadManager's name) is exist, add {@link ILoadManager}.<br />
     * if key is already exist, throw {@link LoadTException}
     *
     * @param name load manager's name
     * @param redirect if true follow redirect
     * @throws LoadTException
     */
    public void setHttpFollowRedirect(@NonNull String name, boolean redirect) throws LoadTException {
        ILoadManager manager = loadManagers.get(name);
        if (manager == null) {
            throw new LoadTException("LoadManager "+name+" is not exist.");
        }
        manager.setHttpFollowRedirect(redirect);
    }

    /**
     * set save interval in millis
     * @param interval in millis
     */
    public void setSaveInterval(long interval) {
        context.setSaveInterval(interval);
    }

    /**
     *
     * @return interval in millis
     */
    public long getSaveInterval() {
        return context.getSaveInterval();
    }

    /**
     * set view interval in millis
     * @param interval in millis
     */
    public void setViewInterval(long interval) {
        context.setViewInterval(interval);
    }

    /**
     *
     * @return interval in millis
     */
    public long getViewInterval() {
        return context.getViewInterval();
    }

    /**
     *
     * after check if key(LoadManager's name) is exist, set loop count.<br />
     * if key is already exist, throw {@link LoadTException}
     *
     * @param name load manager's name
     * @param count loop count
     * @throws LoadTException
     */
    public void setLoopCount(@NonNull String name, int count) throws LoadTException {
        ILoadManager manager = loadManagers.get(name);
        if (manager == null) {
            throw new LoadTException("LoadManager "+name+" is not exist.");
        }
        manager.setLoopCount(count);
    }

    /**
     *
     * after check if key(LoadManager's name) is exist, set loop forever.<br />
     * if key is already exist, throw {@link LoadTException}
     *
     * @param name load manager's name
     * @param forever
     * @throws LoadTException
     */
    public void setLoopForever(@NonNull String name, boolean forever) throws LoadTException {
        ILoadManager manager = loadManagers.get(name);
        if (manager == null) {
            throw new LoadTException("LoadManager "+name+" is not exist.");
        }
        manager.setLoopForever(forever);
    }

    /**
     *
     * after check if key(LoadManager's name) is exist, add {@link HttpSampler}.<br />
     * if key is already exist, throw {@link LoadTException}
     *
    * @param managerName load manager name
    * @param name http sampler name
    * @param domain http domain
    * @param port http port
    * @param path http path
     * @param method {@link HTTPMethod http method}
    * @throws LoadTException
    * @see HttpSampler
    */
    public void addHttpSampler(@NonNull String managerName, String name, String domain, int port, String path, HTTPMethod method, String systemInfoCollectorDomain) throws LoadTException {
        ILoadManager manager = loadManagers.get(managerName);
        if (manager == null) {
            throw new LoadTException("LoadManager "+name+" is not exist.");
        }
        manager.addHttpSampler(name, domain, port, path, method, systemInfoCollectorDomain);
    }

    /**
     * after check if key(LoadManager's name) is exist, add {@link ILoadManager}.<br />
     * if key is already exist, throw {@link LoadTException}
     *
     * @param name load manager name
     * @param type load manager type
     * @throws LoadTException
     */
    public void addLoadManager(@NonNull  String name, @NonNull LoadManagerType type) throws LoadTException {
        if (loadManagers.containsKey(name)) {
            throw new LoadTException("LoadManager "+name+" is already exist.");
        }

        ILoadManager manager;
        if (type.equals(LoadManagerType.JMETER)) {
             manager = new JMeterLoadManager(name);
        } else if (type.equals(LoadManagerType.DUMMY)) {
            manager = new TestLoadManager(name);
        } else {
            throw new LoadTException(type.toString() + " is not supported");
        }

        loadManagers.put(name, manager);
    }

    /**
     * add {@link IResultListener result listener} to load manager's context. If listener is exist in list, throw LostTException
     *
     * @param managerName
     * @param listener ResultListener to be added
     * @throws LoadTException manager not exist, or listener is already exist.
     */
    public void addListener(@NonNull String managerName, @NonNull IResultListener listener) throws LoadTException {
        if (!loadManagers.containsKey(managerName)) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        loadManagers.get(managerName).addListener(listener);
    }

    /**
     *
     * @param managerName load manager name to be removed
     * @return the previous {@link ILoadManager} associated with managerName,
     * or null if there was no mapping for managerName.
     * (A null return can also indicate that the map previously associated null with managerName.)
     */
    public ILoadManager removeLoadManager(@NonNull String managerName) {
        return loadManagers.remove(managerName);
    }

    /**
     * remove all load managers.
     */
    public void removeAllLoadManagers() {
        loadManagers.clear();
    }

    /**
     * add {@link SystemInfoCollector} to global contex.
     *
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    public void addSystemInfoCollector(SystemInfoCollector collector) throws LoadTException {
        context.addSystemInfoCollector(collector);
    }

    /**
     * {@link SystemInfoCollector} with name(domain name) thread start.
     *
     * @param name system collector's domain name
     *
     * @throws LoadTException if SystemInfoCollector with name(domain name) is not exist.
     */
    public void startSystemInfoCollector(String name) throws LoadTException {
        getContext().startSystemInfoCollector(name);
    }

    /**
     * {@link SystemInfoCollector}s
     *
     * @throws LoadTException if SystemInfoCollector is not exist.
     */
    public void startSystemInfoCollectors() throws LoadTException {
        getContext().startSystemInfoCollectors();
    }


    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param managerName
     * @param calculator calculator
     * @throws LoadTException
     */
    public void addCalculator(@NonNull String managerName, @NonNull IResultCalculator calculator) throws LoadTException {
        ILoadManager manager = loadManagers.get(managerName);
        if (manager == null) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        manager.addCalculator(calculator);
    }

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param managerName
     * @param calculatorName calculator name
     * @param listener       ResultListener to be added
     */
    public void addStatisticSampleListener(@NonNull String managerName, @NonNull String calculatorName, @NonNull IResultListener listener) throws LoadTException {
        ILoadManager manager = loadManagers.get(managerName);
        if (manager == null) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        manager.addStatisticSampleListener(calculatorName, listener);
    }

    /**
     *
     * @param saver result saver
     */
    public void addResultSaver(@NonNull String managerName, @NonNull final IResultSaver saver) throws LoadTException {
        ILoadManager manager = loadManagers.get(managerName);
        if (manager == null) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        manager.addResultSaver(saver);
    }

    /**
     *
     * @param managerName load manager name
     * @param numberOfThread number of thread
     * @throws LoadTException
     */
    public void setNumberOfThread(String managerName, int numberOfThread) throws LoadTException {
        if (!loadManagers.containsKey(managerName)) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        loadManagers.get(managerName).setNumberOfThread(numberOfThread);
    }

    /**
     *
     * @param name load manager's name
     * @return load manager with name
     */
    public ILoadManager getLoadManager(@NonNull String name) {
        return loadManagers.get(name);
    }

    /**
     * perform test using {@link ILoadManager#performTest()}
     *
     * @throws LoadTException
     */
    public void runTest(@NonNull String loadManagerName) throws LoadTException {

        ILoadManager loadManager = this.loadManagers.get(loadManagerName);
        if (loadManager == null) throw new LoadTException("LoadManager "+loadManagerName+ " is not exist.");
        loadManager.start();
    }

    /**
     * run all load manager's perform test
     *
     * @see #runTest(String)
     * @throws LoadTException
     */
    public void runTestAll() throws LoadTException, InterruptedException {
        for (String name : loadManagers.keySet()) {
            runTest(name);
            log.debug("\"{}\" test started", name);
        }
        log.debug("all test started");

        for (String name : loadManagers.keySet()) {
            loadManagers.get(name).getThread().join();
        }
    }
}
