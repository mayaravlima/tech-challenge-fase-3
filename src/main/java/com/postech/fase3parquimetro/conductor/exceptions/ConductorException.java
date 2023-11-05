package com.postech.fase3parquimetro.conductor.exceptions;

import com.postech.fase3parquimetro.utils.NoStackTraceApiException;

public class ConductorException extends NoStackTraceApiException {

    public String message;
    public int statusCode;

    public ConductorException(String message) {
        super(message);
    }

    public ConductorException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public ConductorException(String message, int statusCode, Throwable cause) {
        super(message, cause);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
