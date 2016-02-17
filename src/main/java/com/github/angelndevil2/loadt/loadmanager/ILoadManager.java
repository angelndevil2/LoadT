package com.github.angelndevil2.loadt.loadmanager;

import com.github.angelndevil2.loadt.common.*;
import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.listener.IResultSaver;

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
    void setHttpKeepAlive(final boolean keepAlive);

    /**
     * @param redirect if true follow redirect
     */
    void setHttpFollowRedirect(final boolean redirect);

    /**
     * @param count loop count
     */
    void setLoopCount(final int count);

    /**
     * @param forever true, if loop forever
     */
    void setLoopForever(final boolean forever);

    /**
     * @param time thread create time in second
     */
    void setRampUpTime(final int time);

    /**
     * @param numberOfThread
     */
    void setNumberOfThread(final int numberOfThread);

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
    void addHttpSampler(final String name, final String domain, final int port, final String path, final HTTPMethod method, final String systemInfoCollectorDomain) throws LoadTException;

    /**
     * add {@link IResultListener result listener} to load manager context. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addListener(final IResultListener listener) throws LoadTException;

    /**
     * start load manager thread
     */
    void start();

    /**
     * get load manager' thread
     */
    Thread getThread();

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     * @throws LoadTException
     */
    void addCalculator(final IResultCalculator calculator) throws LoadTException;

    /**
     *
     * @param saver result saver
     */
    void addResultSaver(final IResultSaver saver) throws LoadTException;
    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addStatisticSampleListener(final String calculatorName, final IResultListener listener) throws LoadTException;
}
