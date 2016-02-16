package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface IResultCalculator extends Serializable, Runnable {

    /**
     *
     * @return calculator name
     */
    String getName();
    /**
     *
     * @param sample
     * @return calculated statistic sample
     */
    StatisticSample calcSample(ISample sample);

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    void sampleOccurred(ISample sample);

    /**
     * add {@link IResultListener result listener} to listener weak hash map. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addListener(IResultListener listener) throws LoadTException;

    /**
     * send sampling result to listener
     *
     * @param sample
     */
    void sendToListeners(StatisticSample sample);

    void start();
}
