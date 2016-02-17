package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.listener.IResultSaver;
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

    public LoadManager(final String name) throws LoadTException {
        this.name = name;
    }

    /**
     * {@link LoadManagerContext#setHttpKeepAlive(boolean)}
     * @param keepAlive true if use http keepalive
     */
    public void setHttpKeepAlive(final boolean keepAlive) {
        context.setHttpKeepAlive(keepAlive);
    }

    /**
     * {@link LoadManagerContext#setHttpFollowRedirect(boolean)}
     * @param redirect if true follow redirect
     */
    public void setHttpFollowRedirect(final boolean redirect) {
        context.setHttpFollowRedirect(redirect);
    }

    /**
     * {@link LoadManagerContext#setLoopCount(int)}
     * @param count loop count
     */
    public void setLoopCount(final int count) {
        context.setLoopCount(count);
    }

    /**
     * {@link LoadManagerContext#setLoopForever(boolean)}
     * @param forever true, if loop forever
     */
    public void setLoopForever(final boolean forever) {
        context.setLoopForever(forever);
    }

    /**
     * {@link LoadManagerContext#setRampUpTime(int)}
     * @param time thread create time in second
     */
    public void setRampUpTime(final int time) {
        context.setRampUpTime(time);
    }

    /**
     * {@link LoadManagerContext#setNumberOfThread(int)}
     * @param numberOfThread
     */
    public void setNumberOfThread(final int numberOfThread) {
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
    public void addHttpSampler(
            @NonNull final String name,
            @NonNull final String domain,
            final int port,
            @NonNull final String path,
            @NonNull final HTTPMethod method,
            @NonNull final String systemInfoCollectorDomain
    ) throws LoadTException {

        context.addHttpSampler(name, domain, port, path, method, systemInfoCollectorDomain);
    }

    /**
     * add {@link IResultListener result listener} to load manager context. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    public void addListener(@NonNull final IResultListener listener) throws LoadTException {
        getContext().addListener(listener);
    }

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     */
    public void addCalculator(@NonNull final IResultCalculator calculator) throws LoadTException {
        getContext().addCalculator(calculator);
    }

    /**
     *
     * @param saver result saver
     */
    public void addResultSaver(@NonNull final IResultSaver saver) throws LoadTException {
        getContext().addResultSaver(saver);
    }

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener       ResultListener to be added
     */
    public void addStatisticSampleListener(@NonNull final String calculatorName, @NonNull final IResultListener listener) throws LoadTException {
        getContext().addStatisticSampleListener(calculatorName, listener);
    }

    /**
     * prepare test
     *
     * <ol>
     *     <li>separated thread start({@link IResultSaver}, {@link IResultCalculator})</li>
     * </ol>
     * must be overridden and called super.
     */
    public void prepareTest() throws LoadTException {
        if (getContext().getHttpSamplers().isEmpty()) throw new LoadTException("No HttpSampler");

        getContext().startCalculators();
        getContext().startSavers();
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
