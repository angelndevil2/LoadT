package com.github.angelndevil2.loadt.common;

import lombok.Data;

import java.util.Formatter;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
public class StatisticSample implements ISample, Comparable<StatisticSample>{

    private static final long serialVersionUID = -7893549424669108106L;
    public static final String FORMAT = "%s,%d,%d,%.2f,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%s,%d";
    public static final String FORMAT_WITH_SYSTEM_INFO = "%s,%d,%d,%.2f,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d";

    private String name;
    private long timestamp;
    private long count;
    private double avgRate;
    private long min;
    private long max;
    private double deviation;
    private double errorPercentage;
    private double rate;
    private double bytesPerSec;
    private double avgPageBytes;
    private Double cpuBusyPercentage;
    private long totalThread;

    public String toCVSString() {
        Formatter f = new Formatter();
        if (cpuBusyPercentage == null) {
            return f.format(FORMAT,
                    name,
                    timestamp,
                    count,
                    avgRate,
                    min,
                    max,
                    deviation,
                    errorPercentage,
                    rate,
                    bytesPerSec,
                    avgPageBytes,
                    "N/A",
                    totalThread
            ).toString();
        }
        return f.format(FORMAT_WITH_SYSTEM_INFO,
                name,
                timestamp,
                count,
                avgRate,
                min,
                max,
                deviation,
                errorPercentage,
                rate,
                bytesPerSec,
                avgPageBytes,
                cpuBusyPercentage,
                totalThread
        ).toString();
    }

     public int compareTo(StatisticSample o) {
            return this.count - o.count < 0L?-1:(this.count == o.count?0:1);
        }

    /**
     * @return sample to printable string
     */
    public String toPrintableString() {
        return toCVSString();
    }

    /**
     * @return sampler name
     */
    public String getLabel() {
        return name;
    }

    public Double getCpuBusy() {
        return cpuBusyPercentage;
    }
}
