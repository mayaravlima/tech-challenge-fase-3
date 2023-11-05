package com.postech.fase3parquimetro.parking.exceptions;

import com.postech.fase3parquimetro.utils.NoStackTraceApiException;

public class ParkingException extends NoStackTraceApiException {
    public String message;
    public int statusCode;

    public ParkingException(String message) {
        super(message);
    }

    public ParkingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public ParkingException(String message, int statusCode, Throwable cause) {
        super(message, cause);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
