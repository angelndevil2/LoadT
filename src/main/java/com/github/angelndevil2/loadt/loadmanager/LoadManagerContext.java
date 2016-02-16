package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author k, Created on 16. 2. 6.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoadManagerContext extends ContextBase {

    private static final long serialVersionUID = 8458996607118385704L;

    private final HTTPOptions httpOptions = new HTTPOptions();
    private final SaveOptions saveOptions = new SaveOptions();
    private int loopCount = 1;
    private int numberOfThread = 1;
    private int rampUpTime = 1;
    private boolean loopForever = false;

    private transient final ConcurrentHashMap<String, HttpSampler> httpSamplers = new ConcurrentHashMap<String, HttpSampler>();
    private transient final CopyOnWriteArrayList<IResultListener> listeners = new CopyOnWriteArrayList<IResultListener>();
    private transient final ConcurrentHashMap<String, SystemInfoCollector> systemInfoCollectors = new ConcurrentHashMap<String, SystemInfoCollector>();
    private transient final ConcurrentHashMap<String, IResultCalculator> calculators = new ConcurrentHashMap<String, IResultCalculator>();

    /**
     * set {@link HTTPOptions#keepAlive}
     * @param keepAlive true if use http keep alive
     */
    public void setHttpKeepAlive(boolean keepAlive) {
        httpOptions.setKeepAlive(keepAlive);
    }

    /**
     *  set {@link HTTPOptions#followRedirect}
     * @param redirect if true follow redirect
     */
    public void setHttpFollowRedirect(boolean redirect) {
        httpOptions.setFollowRedirect(redirect);
    }

    /**
     * @return true if use http keep alive
     */
    public boolean isHttpKeepAlive() {
        return httpOptions.isKeepAlive();
    }

    /**
     * @return true if use http follow redirect
     */
    public boolean isHttpFollowRedirect() {
        return httpOptions.isFollowRedirect();
    }

    /**
     * add {@link HttpSampler}
     *
     * @param name http sampler name
     * @param domain http domain
     * @param port http port
     * @param path http path
     * @param method HTTPMethod
     * @param systemInfoCollectorDomain SystemInfoCollector domain
     */
    public void addHttpSampler(@NonNull String name, String domain, int port, String path, HTTPMethod method, String systemInfoCollectorDomain) throws LoadTException {
        if (httpSamplers.containsKey(name)) throw new LoadTException("HttpSampler "+ name + " already exist.");
        httpSamplers.put(name, new HttpSampler(name, domain, port, path, method, systemInfoCollectorDomain));
    }

    /**
     * add {@link HttpSampler}
     *
     * @param name http sampler name
     * @param domain http domain
     * @param path http path
     * @param method HTTPMethod
     */
    public void addHttpSampler(@NonNull String name, String domain, String path, HTTPMethod method) throws LoadTException {
        addHttpSampler(name, domain, 80, path, method, null);
    }

    /**
     * add {@link HttpSampler}
     *
     * @param name http sampler name
     * @param domain http domain
     * @param path http path
     * @param method HTTPMethod
     * @param systemInfoCollectorDomain SystemInfoCollector domain
     */
    public void addHttpSampler(@NonNull String name, String domain, String path, HTTPMethod method, String systemInfoCollectorDomain) throws LoadTException {
        addHttpSampler(name, domain, 80, path, method, systemInfoCollectorDomain);
    }

    /**
     * add {@link HttpSampler}
     *
     * @param name http sampler name
     * @param domain http domain
     * @param port http port
     * @param path http path
     */
    public void addHttpSampler(@NonNull String name, String domain, int port, String path) throws LoadTException {
        addHttpSampler(name, domain, port, path, HTTPMethod.GET, null);
    }

    /**
     * add {@link HttpSampler}
     *
     * @param name http sampler name
     * @param domain http domain
     * @param path http path
     */
    public void addHttpSampler(@NonNull String name, String domain, String path) throws LoadTException {
        addHttpSampler(name, domain, 80, path, HTTPMethod.GET, null);
    }

    /**
     * add {@link IResultListener result listener} to listener array list. If listener is exist in list, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    public void addListener(IResultListener listener) throws LoadTException {
        if (listeners.contains(listener)) throw new LoadTException("listener "+listener+" already exist.");
        listeners.add(listener);
    }

    /**
     *
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    public void addSystemInfoCollector(SystemInfoCollector collector) throws LoadTException {
        if (systemInfoCollectors.containsKey(collector.getDomain())) throw new LoadTException("SystemInfoCollector "+collector+" already exist.");
        systemInfoCollectors.put(collector.getDomain(), collector);
    }

    /**
     *
     * @param name http sampler name
     * @return http sampler
     */
    public HttpSampler getHttpSampler(String name) {
        return httpSamplers.get(name);
    }

    /**
     *
     * @param systemInfoCollectorDomain system informatin collector domain
     *
     * @return SystemInfoCollector
     */
    public SystemInfoCollector getSystemInfoCollector(String systemInfoCollectorDomain) {
        return systemInfoCollectors.get(systemInfoCollectorDomain);
    }

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     */
    public void addCalculator(@NonNull IResultCalculator calculator) throws LoadTException {
        String name = calculator.getName();
        if (calculators.containsKey(name)) throw new LoadTException("calculator "+name+" already exist.");
        calculators.put(name, calculator);
    }

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener       ResultListener to be added
     */
    public void addStatisticSampleListener(@NonNull String calculatorName, @NonNull IResultListener listener) throws LoadTException {
        IResultCalculator calculator = calculators.get(calculatorName);
        if (calculator == null) throw new LoadTException("calculator "+calculatorName+" is not exist.");

        calculator.addListener(listener);
    }
}
