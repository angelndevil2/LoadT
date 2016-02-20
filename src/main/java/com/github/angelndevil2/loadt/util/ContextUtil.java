package com.github.angelndevil2.loadt.util;

import com.github.angelndevil2.loadt.LoadT;
import com.github.angelndevil2.loadt.LoadTContext;
import com.github.angelndevil2.loadt.common.SystemInfoCollector;
import com.github.angelndevil2.loadt.loadmanager.ILoadManager;
import com.github.angelndevil2.loadt.loadmanager.LoadManagerContext;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * Context utility class
 *
 * @author k, Created on 16. 2. 6.
 */
@Data
public class ContextUtil implements Serializable {

    private static final long serialVersionUID = -1042965567642438603L;

    private static final LoadTContext globalContext = LoadT.getContext();
    private static final InheritableThreadLocal<LoadManagerContext> context = new InheritableThreadLocal<LoadManagerContext>();

    @Setter
    private static LoadT loadT;

    /**
     *
     * @param ctx load manager context to be set to thread local
     */
    public static void setLoadManagerContext(@NonNull final LoadManagerContext ctx) {
        context.set(ctx);
    }

    /**
     *
     * @return load manager context in thread local
     */
    public static LoadManagerContext getLoadManagerContext() {
        return context.get();
    }

    /**
     *
     * @return save interval in millis
     */
    public static long getSaveInterval() {
        return globalContext.getSaveInterval();
    }

    /**
     *
     * @return interval in millis
     */
    public static long getViewInterval() {
        return globalContext.getViewInterval();
    }

    /**
     *
     * @param domainName rmi domain name which used by SystemInfoCollector
     * @return SystemInfoCollector
     */
    public static SystemInfoCollector getSystemInfoCollector(@NonNull final String domainName) {
        return globalContext.getSystemInfoCollector(domainName);
    }

    /**
     * @since 0.0.2
     * @return set of load manager's names
     */
    public static Set<String> getLoadManagers() {
        return loadT == null ? null : loadT.getLoadManagers().keySet();
    }

    /**
     * @since 0.0.2
     * @param name load manager name
     * @return load manager with name
     */
    public static ILoadManager getLoadManager(@NonNull String name) {
        return loadT == null ? null : loadT.getLoadManager(name);
    }
}
