package com.github.angelndevil2.loadt.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JMeterSampleResult extends org.apache.jmeter.samplers.SampleResult implements ISample {

    private static final long serialVersionUID = -5189165209227400658L;
    private Double cpuBusy;

    public JMeterSampleResult(org.apache.jmeter.samplers.SampleResult result) {
        super(result);
    }

    public String toPrintableString() {
        StringBuilder sb = new StringBuilder();
        sb.append("all thread : ").append(this.getAllThreads()).append("\n");
        sb.append(this.getAssertionResults()).append("\n");
        sb.append("bytes : ").append(this.getBytes()).append("\n");
        sb.append("header size : ").append(this.getHeadersSize()).append("\n");
        sb.append("body size : ").append(this.getBodySize()).append("\n");
        sb.append("content type : ").append(this.getContentType()).append("\n");
        sb.append("data encoding : ").append(this.getDataEncodingNoDefault()).append("\n");
        sb.append("data type : ").append(this.getDataType()).append("\n");
        sb.append("end time : ").append(this.getEndTime()).append("\n");
        sb.append("groupThreads : ").append(this.getGroupThreads()).append("\n");
        sb.append("idle time : ").append(this.getIdleTime()).append("\n");
        sb.append("is monitor : ").append(this.isMonitor()).append("\n");
        sb.append("label : ").append(this.getSampleLabel()).append("\n");
        sb.append("latency : ").append(this.getLatency()).append("\n");
        sb.append("connect time : ").append(this.getConnectTime()).append("\n");
        sb.append("request headers : ").append(this.getRequestHeaders()).append("\n");
        sb.append("response code : ").append(this.getResponseCode()).append("\n");
        sb.append("response data : ").append(this.getResponseDataAsString()).append("\n");
        sb.append("response headers : ").append(this.getResponseHeaders()).append("\n");
        sb.append("response message : ").append(this.getResponseMessage()).append("\n");
        sb.append("sample count : ").append(this.getSampleCount()).append("\n");
        sb.append("sampler data : ").append(this.getSamplerData()).append("\n");
        sb.append("start time : ").append(this.getStartTime()).append("\n");
        sb.append("stop test : ").append(this.isStopTest()).append("\n");
        sb.append("stop test now : ").append(this.isStopTestNow()).append("\n");
        sb.append("stop thread : ").append(this.isStopThread()).append("\n");
        sb.append("start next thread loop : ").append(this.isStartNextThreadLoop()).append("\n");
        for (org.apache.jmeter.samplers.SampleResult r : this.getSubResults())
            sb.append("\t").append(r).append("\n");

        sb.append("success : ").append(this.isSuccessful()).append("\n");
        sb.append("thread name : ").append(this.getThreadName()).append("\n");
        sb.append("elapsed time : ").append(this.getTime()).append("\n");
        sb.append("time stamp : ").append(this.getTimeStamp()).append("\n");
        sb.append("cpu busy : ").append(this.getCpuBusy()).append("\n");

        return sb.toString();
    }

    /**
     * @return sampler name
     */
    public String getLabel() {
        return getSampleLabel();
    }
}
