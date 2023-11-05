package com.postech.fase3parquimetro.receipt.model;

import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record ReceiptReadRecord(
        String conductorName,
        String vehicleModel,
        String vehiclePlate,
        String paymentMethod,
        LocalDateTime createdAt,
        String duration,
        BigDecimal price
) {

    public static ReceiptReadRecord with(ReceiptEntity receiptEntity) {
        final var conductor = receiptEntity.getConductor();
        final var vehicle = receiptEntity.getVehicle();
        final var payment = receiptEntity.getPayment();
        final var parking = receiptEntity.getParking();

        final var parkingCreation = parking.getCreatedAt();
        final var parkingEnding = parking.getExpiresIn();
        final var duration = Duration.between(parkingCreation, parkingEnding);

        return new ReceiptReadRecord(
                conductor.getName(),
                vehicle.getModel(),
                vehicle.getPlate(),
                payment.getPaymentType().toString(),
                LocalDateTime.now(),
                String.format("%s:%s", duration.toHoursPart(), duration.toMinutesPart()),
                parking.getPrice());
    }
}
