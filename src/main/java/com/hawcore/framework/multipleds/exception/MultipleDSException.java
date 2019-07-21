package com.hawcore.framework.multipleds.exception;

public class MultipleDSException extends RuntimeException {

    public MultipleDSException() {
        super();
    }

    public MultipleDSException(String message) {
        super(message);
    }

    public MultipleDSException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleDSException(Throwable cause) {
        super(cause);
    }

    protected MultipleDSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
