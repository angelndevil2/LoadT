package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.HTTPMethod;
import com.github.angelndevil2.loadt.common.HttpSampler;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.SystemInfoCollector;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.loadmanager.ILoadManager;
import com.github.angelndevil2.loadt.loadmanager.JMeterLoadManager;
import com.github.angelndevil2.loadt.loadmanager.LoadManagerType;
import com.github.angelndevil2.loadt.loadmanager.TestLoadManager;
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
     * @param managerName load manager name
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    public void addSystemInfoCollector(String managerName, SystemInfoCollector collector) throws LoadTException {
        if (!loadManagers.containsKey(managerName)) throw new LoadTException("LoadManager " + managerName + " is not exist.");
        loadManagers.get(managerName).addSystemInfoCollector(collector);
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
