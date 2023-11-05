package com.postech.fase3parquimetro.utils;

public class NoStackTraceApiException extends RuntimeException {

    public NoStackTraceApiException(String message) {
        this(message, null);
    }

    public NoStackTraceApiException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
