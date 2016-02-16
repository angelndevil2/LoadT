package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import lombok.NonNull;

/**
 * name is the key, so constructor with name is necessary.
 *
 * @author k, Created on 16. 2. 5.
 */
public interface ILoadManager extends Runnable {

    /**
     * @return LoadManagerType
     */
    LoadManagerType getType();

    /**
     * prepare test
     */
    void prepareTest() throws LoadTException;

    /**
     * run test actually
     */
    void performTest();

    /**
     * @return each LoadManager's context
     */
    LoadManagerContext getContext();

    /**
     * @return loadmanager's name
     */
    String getName();

    /**
     * @param keepAlive true if use http keepalive
     */
    void setHttpKeepAlive(boolean keepAlive);

    /**
     * @param redirect if true follow redirect
     */
    void setHttpFollowRedirect(boolean redirect);

    /**
     * @param count loop count
     */
    void setLoopCount(int count);

    /**
     * @param forever true, if loop forever
     */
    void setLoopForever(boolean forever);

    /**
     * @param time thread create time in second
     */
    void setRampUpTime(int time);

    /**
     * @param numberOfThread
     */
    void setNumberOfThread(int numberOfThread);

    /**
     * @return true if use http keep alive
     */
    boolean isHttpKeepAlive();

    /**
     * @return true if use http follow redirect
     */
    boolean isHttpFollowRedirect();

    /**
     *
     * @see com.github.angelndevil2.loadt.common.HttpSampler
     *
     * @param name http sampler name
     * @param domain http domain
     * @param port http port
     * @param path http path
     * @param method {@link HTTPMethod http method}
     */
    void addHttpSampler(String name, String domain, int port, String path, HTTPMethod method, String systemInfoCollectorDomain) throws LoadTException;

    /**
     * add {@link IResultListener result listener} to load manager context. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addListener(IResultListener listener) throws LoadTException;

    /**
     * start load manager thread
     */
    void start();

    /**
     * get load manager' thread
     */
    Thread getThread();

    /**
     *
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    void addSystemInfoCollector(SystemInfoCollector collector) throws LoadTException;

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     * @throws LoadTException
     */
    void addCalculator(@NonNull IResultCalculator calculator) throws LoadTException;

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addStatisticSampleListener(@NonNull String calculatorName, @NonNull IResultListener listener) throws LoadTException;
}
