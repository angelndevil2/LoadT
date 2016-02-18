package com.github.angelndevil2.loadt.jetty;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 18.
 */
public class ServiceUri implements Serializable {
    private static final long serialVersionUID = -4607093523913813997L;

    public static class Uri4Get {
        /**
         * /LoadT/info
         */
        public static final String INFO = "info";
        /**
         * /LoadT/load-managers
         */
        public static final String LOAD_MANAGERS = "load-managers";
        /**
         * for each load manager
         */
        public static final String LOAD_MANAGER = "load-manager";

        public static class LoadManager {
            /**
             * /LoadT/load-manager/name/http-options
             */
            public static final String HTTP_OPTIONS = "http-options";
        }
    }
}
