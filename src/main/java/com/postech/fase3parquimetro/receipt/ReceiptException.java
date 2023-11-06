package com.postech.fase3parquimetro.receipt;

import com.postech.fase3parquimetro.utils.NoStackTraceApiException;

public class ReceiptException extends NoStackTraceApiException {
    public String message;
    public int statusCode;

    public ReceiptException(String message) {
        super(message);
    }

    public ReceiptException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public ReceiptException(String message, int statusCode, Throwable cause) {
        super(message, cause);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
