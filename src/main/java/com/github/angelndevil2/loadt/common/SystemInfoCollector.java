package com.github.angelndevil2.loadt.common;

import com.tistory.devilnangel.systeminfo.client.RmiSystemInfoClient;
import com.tistory.devilnangel.systeminfo.common.IRmiCpuInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;

/**
 *
 * Collect system information using <a href="https://github.com/angelndevil2/system-info">system-info</a>
 *
 * @author k, Created on 16. 1. 31.
 */
@Data
@Slf4j
public class SystemInfoCollector implements Runnable {

    public static final long COLLECT_INTERVAL = 1000;
    private IRmiCpuInfo cpuInfo;
    private final String domain;
    private volatile Double cpuBusy = null;

    public SystemInfoCollector(final String host) {
        domain = host;
    }

    @Override
    public void run() {

        if (domain == null) {
            cpuBusy = null;
            return;
        }

        try {
            cpuInfo = new RmiSystemInfoClient(domain).getCpuInfo();
        } catch (Exception e) {
            log.error("{} remote exception", domain, e);
            cpuBusy = null;
            return;
        }

        while (true) {
            try {
                cpuBusy = cpuInfo.getCpuBusy();
                Thread.sleep(COLLECT_INTERVAL);
            } catch (RemoteException e) {
                log.error("{} remote exception", domain, e);
                cpuBusy = null;
                return;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}