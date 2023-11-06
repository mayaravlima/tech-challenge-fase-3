package com.postech.fase3parquimetro.parking.model;

import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import jakarta.validation.constraints.NotNull;

public record ParkingCreateOrUpdateRecord(
        int durationInMinutes,
        @NotNull(message = "Parking type can't be empty or null")
        String parkingType,
        PaymentEntity payedWith
) {
}
