package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.WeakHashMap;

/**
 * @author k, Created on 16. 2. 17.
 */
@Data
public class ResultCollector implements IResultCollector {

    private static final long serialVersionUID = -2779880260423052037L;

    @Getter(AccessLevel.NONE)
    private final transient WeakHashMap<IResultListener, Void> listeners = new WeakHashMap<IResultListener, Void>();
    @Getter(AccessLevel.NONE)
    private final transient WeakHashMap<String, IResultCalculator> calculators = new WeakHashMap<String, IResultCalculator>();

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    @Override
    public void sampleOccurred(@NonNull ISample sample) {
        sendToCalculators(sample);
        sendToListeners(sample);
    }

    /**
     * send sampling result to listener
     *
     * @param sample
     */
    @Override
    public void sendToListeners(@NonNull ISample sample) {
        for (IResultListener listener : listeners.keySet()) {
            listener.sampleOccurred(sample);
        }
    }

    /**
     * add {@link IResultListener result listener} to listener weak hash map. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
    @Override
    public void addListener(@NonNull IResultListener listener) throws LoadTException {
        if (listeners.containsKey(listener)) throw new LoadTException("listener "+listener+" already exist.");
        listeners.put(listener, null);
    }

    /**
     * get cpu busy percentage using {@link SystemInfoCollector system information collector} with sample's name.
     *
     * @param sample
     * @return cpu busy percentage. null if system information collector is not available.
     */
    @Override
    public Double getCpuBusy(@NonNull ISample sample) {
        String index = sample.getLabel();
        HttpSampler sampler = ContextUtil.getLoadManagerContext().getHttpSampler(index);
        if (sampler == null) return null;
        SystemInfoCollector systemInfoCollector = ContextUtil.getLoadManagerContext().getSystemInfoCollector(sampler.getSystemInfoCollectorDomain());
        if (systemInfoCollector == null) return null;

        return systemInfoCollector.getCpuBusy();
    }

    /**
     * add {@link IResultCalculator calculator} for statistic data
     *
     * @param calculator calculator
     * @throws LoadTException
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
    @Override
    public void addStatisticSampleListener(@NonNull String calculatorName, @NonNull IResultListener listener) throws LoadTException {
        IResultCalculator calculator = calculators.get(calculatorName);
        if (calculator == null) throw new LoadTException("calculator "+calculatorName+" is not exist.");

        calculator.addListener(listener);
    }

    /**
     * send sampling result to calculators.
     *
     * @param sample
     */
    @Override
    public void sendToCalculators(ISample sample) {
        for (IResultCalculator calculator : calculators.values()) {
            calculator.sampleOccurred(sample);
        }
    }
}
