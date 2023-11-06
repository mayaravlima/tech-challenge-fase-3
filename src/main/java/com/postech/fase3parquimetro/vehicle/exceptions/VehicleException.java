package com.postech.fase3parquimetro.vehicle.exceptions;

import com.postech.fase3parquimetro.utils.NoStackTraceApiException;

public class VehicleException extends NoStackTraceApiException {

    public String message;
    public int statusCode;

    public VehicleException(String message) {
        super(message);
    }

    public VehicleException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public VehicleException(String message, int statusCode, Throwable cause) {
        super(message, cause);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
