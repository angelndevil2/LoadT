package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.Calculator;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author k, Created on 16. 2. 16.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class JMeterCalculator extends Calculator implements IResultCalculator{

    private static final long serialVersionUID = -1653420503046132283L;

    private Thread thread;

    @Getter(AccessLevel.NONE)
    private final transient HashMap<IResultListener, Void> listeners = new HashMap<IResultListener, Void>();
    @Getter(AccessLevel.NONE)
    private transient final ArrayBlockingQueue<ISample> q = new ArrayBlockingQueue<ISample>(100000);

    public JMeterCalculator(String label) {
        super(label);
    }

    /**
     * @return calculator name
     */
    @Override
    public String getName() {
        return getLabel();
    }

    public StatisticSample calcSample(@NonNull ISample sample) {
        SampleResult result = (SampleResult)sample;
        addSample(result);
        StatisticSample statisticSample = new StatisticSample();
        statisticSample.setName(getLabel());
        statisticSample.setRate(getRate());
        statisticSample.setAvgRate(getMean());
        statisticSample.setDeviation(getStandardDeviation());
        statisticSample.setErrorPercentage(getErrorPercentage());
        statisticSample.setBytesPerSec(getBytesPerSecond());
        statisticSample.setAvgPageBytes(getAvgPageBytes());
        statisticSample.setCpuBusyPercentage(sample.getCpuBusy());
        statisticSample.setCount(getCount());
        statisticSample.setMax(getMax());
        statisticSample.setMin(getMin());
        statisticSample.setTotalThread(result.getAllThreads());
        statisticSample.setTimestamp(result.getTimeStamp());
        return statisticSample;
    }

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    @Override
    public void sampleOccurred(@NonNull ISample sample) {
        log.debug("sample {} occurred", sample);
        q.offer(sample);
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
        log.debug("{} added", listener);
    }

    /**
     * send sampling result to listener
     *
     * @param sample
     */
    @Override
    public void sendToListeners(@NonNull StatisticSample sample) {
        for (IResultListener listener : listeners.keySet()) {
            listener.sampleOccurred(sample);
            log.debug("{} send to {}", sample, listener);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                sendToListeners(calcSample(q.take()));
            } catch (InterruptedException e) {
                log.info("interrupted.", e);
                return;
            }
        }
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
