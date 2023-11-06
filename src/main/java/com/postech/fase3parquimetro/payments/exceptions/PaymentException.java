package com.postech.fase3parquimetro.payments.exceptions;

import com.postech.fase3parquimetro.utils.NoStackTraceApiException;

public class PaymentException extends NoStackTraceApiException {

    public String message;
    public int statusCode;

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
