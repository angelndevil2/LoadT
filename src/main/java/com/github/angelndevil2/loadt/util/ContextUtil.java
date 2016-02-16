package com.github.angelndevil2.loadt.util;

import com.github.angelndevil2.loadt.LoadT;
import com.github.angelndevil2.loadt.LoadTContext;
import com.github.angelndevil2.loadt.loadmanager.LoadManagerContext;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

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

    public static void setLoadManagerContext(LoadManagerContext ctx) {
        context.set(ctx);
    }

    public static LoadManagerContext getLoadManagerContext() {
        return context.get();
    }
}
