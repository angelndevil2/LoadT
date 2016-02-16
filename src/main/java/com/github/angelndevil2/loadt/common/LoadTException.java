package com.github.angelndevil2.loadt.common;

/**
 * LoadT  Exception class
 * @author k, Created on 16. 2. 5.
 */
public class LoadTException extends Exception {
    private static final long serialVersionUID = -5062603276741550725L;

    public LoadTException() {
    }

    public LoadTException(String message) {
        super(message);
    }

    public LoadTException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadTException(Throwable cause) {
        super(cause);
    }

    protected LoadTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
