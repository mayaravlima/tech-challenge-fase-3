package com.postech.fase3parquimetro.domain.paymentMethod.enums;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PIX;

    public static PaymentMethod fromString(String name) {
        return name == null
                ? null
                : PaymentMethod.valueOf(name.toUpperCase());
    }


}
