package com.postech.fase3parquimetro.payments.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record CardCreateOrUpdateRecord(
        @NotNull @CreditCardNumber
        String cardNumber,

        @NotNull @NotEmpty
        String cardHolder,

        @NotNull @Pattern(regexp = "[0-9]{3}")
        String cardCvv,

        @NotNull @NotEmpty
        String expirationDate,

        @NotNull
        String cardType,

        boolean isFavorite
) {
}
