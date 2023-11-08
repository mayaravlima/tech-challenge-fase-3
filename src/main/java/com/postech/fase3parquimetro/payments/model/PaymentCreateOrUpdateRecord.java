package com.postech.fase3parquimetro.payments.model;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;

public record PaymentCreateOrUpdateRecord(
        @CreditCardNumber(message = "Card number must be valid")
        String cardNumber,

        String cardHolder,

        @Pattern(regexp = "[0-9]{3}", message = "CVV must contain only 3 digits")
        String cardCvv,

        @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2})$", message = "Expiration date must be valid ex: 12/23")
        String expirationDate,

        PaymentTypeEnum paymentType,

        boolean isFavorite
) {
}
