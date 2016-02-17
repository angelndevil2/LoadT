package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author k, Created on 16. 2. 17.
 */
@Data
@Slf4j
public abstract class ResultSaver implements IResultSaver {

    private static final long serialVersionUID = 3369200840659977754L;
    private long lastSaveTime = System.currentTimeMillis();

    @Data
    private final class ShutdownHandler implements Runnable {
        @Override
        public void run() {
            try {
                close();
            } catch (LoadTException e) {
                log.info("closing exception",e);
            }
        }
    }

    @Getter(AccessLevel.NONE)
    private final transient HashMap<IResultListener, Void> listeners = new HashMap<IResultListener, Void>();
    @Getter(AccessLevel.NONE)
    private final transient ArrayBlockingQueue<ISample> q = new ArrayBlockingQueue<ISample>(100000);

    private Thread thread;

    /**
     * called when sample is occurred.
     *
     * @param sample
     */
    @Override
    public void sampleOccurred(@NonNull final ISample sample) {
        q.offer(sample);
    }

    /**
     * @return save interval in millis
     */
    @Override
    public long getSaveInterval() {
        return ContextUtil.getSaveInterval();
    }

    /**
     * check {@link com.github.angelndevil2.loadt.common.SaveOptions} and save or not
     *
     */
    @Override
    public void saveOrNot(ISample sample) throws LoadTException {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastSaveTime > getSaveInterval()) {
            save(sample);
            lastSaveTime = currentTime;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                saveOrNot(q.take());
            } catch (InterruptedException e) {
                log.info("interrupted", e);
                return;
            } catch (LoadTException e) {
                log.info("save exception",e);
                return;
            }
        }
    }

    /**
     * start thread
     */
    @Override
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHandler()));
    }
}
