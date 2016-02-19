package com.github.angelndevil2.loadt.common;

import com.github.angelndevil2.loadt.listener.IResultListener;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author k, Created on 16. 2. 17.
 */
@Slf4j
@Getter @Setter
@EqualsAndHashCode(of = {"name", "thread"})
@ToString(of = {"name", "thread"})
public abstract class ResultCalculator implements IResultCalculator {

    private static final long serialVersionUID = -8973924537201333735L;

    private String name;
    private Thread thread;

    @Getter(AccessLevel.NONE)
    private final transient HashMap<IResultListener, Void> listeners = new HashMap<IResultListener, Void>();
    @Getter(AccessLevel.NONE)
    private transient final ArrayBlockingQueue<ISample> q = new ArrayBlockingQueue<ISample>(100000);

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    @Override
    public void sampleOccurred(@NonNull ISample sample) {
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
