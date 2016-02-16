package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author k, Created on 16. 2. 13.
 */
@Data
@Slf4j
public abstract class LoadManager implements ILoadManager {

    private final String name;
    private Thread thread;
    private final LoadManagerContext context = new LoadManagerContext();

    public LoadManager(String name) throws LoadTException {
        this.name = name;
    }

    /**
     * {@link LoadManagerContext#setHttpKeepAlive(boolean)}
     * @param keepAlive true if use http keepalive
     */
    public void setHttpKeepAlive(boolean keepAlive) {
        context.setHttpKeepAlive(keepAlive);
    }

    /**
     * {@link LoadManagerContext#setHttpFollowRedirect(boolean)}
     * @param redirect if true follow redirect
     */
    public void setHttpFollowRedirect(boolean redirect) {
        context.setHttpFollowRedirect(redirect);
    }

    /**
     * {@link LoadManagerContext#setLoopCount(int)}
     * @param count loop count
     */
    public void setLoopCount(int count) {
        context.setLoopCount(count);
    }

    /**
     * {@link LoadManagerContext#setLoopForever(boolean)}
     * @param forever true, if loop forever
     */
    public void setLoopForever(boolean forever) {
        context.setLoopForever(forever);
    }

    /**
     * {@link LoadManagerContext#setRampUpTime(int)}
     * @param time thread create time in second
     */
    public void setRampUpTime(int time) {
        context.setRampUpTime(time);
    }

    /**
     * {@link LoadManagerContext#setNumberOfThread(int)}
     * @param numberOfThread
     */
    public void setNumberOfThread(int numberOfThread) {
        context.setNumberOfThread(numberOfThread);
    }

    /**
     * @return true if use http keep alive
     */
    public boolean isHttpKeepAlive() {
        return context.isHttpKeepAlive();
    }

    /**
     * @return true if use http follow redirect
     */
    public boolean isHttpFollowRedirect() {
        return context.isHttpFollowRedirect();
    }

    /**
     * @param name http sampler name
     * @param domain http domain
     * @param port http port
     * @param path http path
     * @param method {@link HTTPMethod http method}
     * @see HttpSampler
     */
    public void addHttpSampler(String name, String domain, int port, String path, HTTPMethod method, String SystemInfoCollectorDomain) throws LoadTException {
        context.addHttpSampler(name, domain, port, path, method, SystemInfoCollectorDomain);
    }

    /**
     * add {@link IResultListener result listener} to load manager context. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    public void addListener(IResultListener listener) throws LoadTException {
        getContext().addListener(listener);
    }

    /**
     *
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    public void addSystemInfoCollector(SystemInfoCollector collector) throws LoadTException {
        getContext().addSystemInfoCollector(collector);
    }

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     */
    public void addCalculator(@NonNull IResultCalculator calculator) throws LoadTException {
        getContext().addCalculator(calculator);
    }

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener       ResultListener to be added
     */
    public void addStatisticSampleListener(@NonNull String calculatorName, @NonNull IResultListener listener) throws LoadTException {
        getContext().addStatisticSampleListener(calculatorName, listener);
    }

    /**
     * prepare test
     *
     * must be overridden and called super.
     */
    public void prepareTest() throws LoadTException {
        if (getContext().getHttpSamplers().isEmpty()) throw new LoadTException("No HttpSampler");
    }

    /**
     * start load manager thread
     */
    public void start() {
        thread = new Thread(this);
        thread.setName(name);
        thread.start();
    }

    public void run() {
        try {
            ContextUtil.setLoadManagerContext(context);
            prepareTest();
            performTest();
        } catch (LoadTException e) {
            log.error(e.getMessage(), e);
        }
    }
}
