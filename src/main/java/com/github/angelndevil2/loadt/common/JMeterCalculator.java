package com.github.angelndevil2.loadt.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.Calculator;

/**
 * @author k, Created on 16. 2. 16.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JMeterCalculator extends Calculator implements IResultCalculator{

    private static final long serialVersionUID = -1653420503046132283L;

    public JMeterCalculator(String label) {
        super(label);
    }

    public StatisticSample calcSample(ISample sample) {
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
}
