package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.samplers.SampleEvent;

import java.util.WeakHashMap;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class JMeterResultCollector extends org.apache.jmeter.reporters.ResultCollector implements IResultCollector {

    private static final long serialVersionUID = 7605139400548030368L;

    @Getter(AccessLevel.NONE)
    private final transient WeakHashMap<IResultListener, Void> listeners = new WeakHashMap<IResultListener, Void>();
    @Getter(AccessLevel.NONE)
    private final transient WeakHashMap<String, IResultCalculator> calculators = new WeakHashMap<String, IResultCalculator>();

    /**
     * called when sample is occurred.
     *
     * @param event
     */
    @Override
    public void sampleOccurred(@NonNull SampleEvent event) {
        sampleOccurred(new JMeterSampleResult(event.getResult()));
    }

    /**
     * called when sample is occurred.
     *
     * @param result
     */
    public void sampleOccurred(@NonNull ISample result) {

        JMeterSampleResult jMeterSampleResult = (JMeterSampleResult)result;
        if (isSampleWanted(jMeterSampleResult.isSuccessful())) {
            jMeterSampleResult.setCpuBusy(getCpuBusy(result));
            sendToListeners(result);
            sendToCalculators(result);
        }
    }

    /**
     * send sampling result to listener
     *
     * @param result
     */
    public void sendToListeners(@NonNull ISample result) {
        for (IResultListener listener : listeners.keySet()) {
            listener.sampleOccurred(result);
        }
    }

    /**
     * add {@link IResultListener result listener} to listener weak hash map. If listener is exist in map, throw LostTException
     *
     * @param listener ResultListener to be added
     * @throws LoadTException
     */
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
     */
    @Override
    public void addCalculator(@NonNull IResultCalculator calculator) throws LoadTException {
        String name = calculator.getName();
        if (calculators.containsKey(name)) throw new LoadTException("calculator "+name+" already exist.");
        calculators.put(name, calculator);
    }

    /**
     * send sampling result to calculators.
     *
     * @param sample
     */
    @Override
    public void sendToCalculators(ISample sample) {
        for (IResultCalculator calculator : calculators.values()) {
            log.debug("sample {} sent to calculator {}", sample, calculator);
            calculator.sampleOccurred(sample);
        }
    }
}
