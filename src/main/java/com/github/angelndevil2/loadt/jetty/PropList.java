package com.github.angelndevil2.loadt.jetty;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 18.
 * @since 0.0.2
 */
public class PropList implements Serializable {
    private static final long serialVersionUID = -8692105702208443640L;

    /**
     * Jetty server's max thread number
     */
    public static final String THREAD_MAX = "thread.max";
    /**
     * http service port
     */
    public static final String HTTP_PORT = "http.port";
}
