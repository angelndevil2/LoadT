package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface IResultCollector extends Serializable {

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    void sampleOccurred(ISample sample);

    /**
     * send sampling result to listener
     *
     * @param sample
     */
    void sendToListeners(ISample sample);

    /**
     * add {@link IResultListener result listener} to listener weak hash map. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addListener(IResultListener listener) throws LoadTException;

    /**
     * get cpu busy percentage using {@link SystemInfoCollector system information collector} with sample's name.
     *
     * @param sample
     * @return cpu busy percentage. null if system information collector is not available.
     */
    Double getCpuBusy(ISample sample);

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     * @throws LoadTException
     */
    void addCalculator(IResultCalculator calculator) throws LoadTException;

    /**
     * add listener to calculator with name which need {@link StatisticSample statistic sample}
     *
     * @param calculatorName calculator name
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    void addStatisticSampleListener(String calculatorName, IResultListener listener) throws LoadTException;

    /**
     * send sampling result to calculators.
     *
     * @param sample
     */
    void sendToCalculators(ISample sample);
}
