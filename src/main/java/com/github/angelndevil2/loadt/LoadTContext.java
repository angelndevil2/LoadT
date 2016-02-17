package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.common.ContextBase;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.SaveOptions;
import com.github.angelndevil2.loadt.common.SystemInfoCollector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * data structure for testing options & environment
 *
 * @author k, Created on 16. 2. 6.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoadTContext extends ContextBase {

    private static final long serialVersionUID = -3020232893612521700L;

    private final SaveOptions saveOptions = new SaveOptions();
    private transient final ConcurrentHashMap<String, SystemInfoCollector> systemInfoCollectors = new ConcurrentHashMap<String, SystemInfoCollector>();

    /**
     *
     * @param systemInfoCollectorDomain system informatin collector domain
     *
     * @return SystemInfoCollector
     */
    public SystemInfoCollector getSystemInfoCollector(@NonNull final String systemInfoCollectorDomain) {
        return systemInfoCollectors.get(systemInfoCollectorDomain);
    }

    /**
     *
     * @param collector system information collector to be added
     * @throws LoadTException
     */
    public void addSystemInfoCollector(@NonNull final SystemInfoCollector collector) throws LoadTException {
        if (systemInfoCollectors.containsKey(collector.getDomain())) throw new LoadTException("SystemInfoCollector "+collector+" already exist.");
        systemInfoCollectors.put(collector.getDomain(), collector);
    }

    /**
     * {@link SystemInfoCollector} with name(domain name) thread start.
     *
     * @param name system collector's domain name
     *
     * @throws LoadTException if SystemInfoCollector with name(domain name) is not exist.
     */
    public void startSystemInfoCollector(String name) throws LoadTException {
        SystemInfoCollector sic = systemInfoCollectors.get(name);
        if (name == null) throw new LoadTException("System information collector ("+name+") is not exist.");
         sic.start();
    }

    /**
     * set save interval in millis
     * @param interval in millis
     */
    public void setSaveInterval(long interval) {
        saveOptions.setSaveInterval(interval);
    }

    /**
     *
     * @return interval in millis
     */
    public long getSaveInterval() {
        return saveOptions.getSaveInterval();
    }

    /**
     * {@link SystemInfoCollector}s
     *
     * @throws LoadTException if SystemInfoCollector is not exist.
     */
    public void startSystemInfoCollectors() throws LoadTException {
        for (String name : systemInfoCollectors.keySet()) startSystemInfoCollector(name);
    }
}
