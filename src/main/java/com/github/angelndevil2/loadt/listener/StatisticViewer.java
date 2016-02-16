package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.IResultCalculator;
import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.ResultType;
import com.github.angelndevil2.loadt.common.StatisticSample;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class StatisticViewer extends ResultViewer implements Runnable {

    private static final long serialVersionUID = 3643254266110216616L;

    private StatisticSample statistics;
    private IResultCalculator totalCalculator;

    @Setter(AccessLevel.NONE)
    private Thread thread;

    @Getter(AccessLevel.NONE)
    private transient final ArrayBlockingQueue<ISample> q = new ArrayBlockingQueue<ISample>(100000);

    @Override
    public ResultType getResultType() {
        return ResultType.STATISTIC;
    }

    @Override
    public void sampleOccurred(@NonNull ISample result) {
        q.offer(result);
    }

    public void run() {
        while (true) {
            try {
                statistics = totalCalculator.calcSample(q.take());
                view();
            } catch (InterruptedException e) {
                log.info("{} interrupted.", toString());
                break;
            }
        }
    }

    /**
     * start this daemon thread
     */
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void interrupt() {
        thread.interrupt();
    }
}
