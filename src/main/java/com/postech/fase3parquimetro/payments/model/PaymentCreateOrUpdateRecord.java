package com.postech.fase3parquimetro.payments.model;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;

public record PaymentCreateOrUpdateRecord(
        @CreditCardNumber (message = "Card number must be valid")
        String cardNumber,

        String cardHolder,

        @Pattern(regexp = "[0-9]{3}", message = "CVV must contain only 3 digits")
        String cardCvv,

        String expirationDate,

        PaymentTypeEnum paymentType,

        boolean isFavorite
) {
}
