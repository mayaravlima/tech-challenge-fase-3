package com.postech.fase3parquimetro.conductor.model;

import com.postech.fase3parquimetro.payments.model.CardCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ConductorCreateOrUpdateRecord(
        @NotBlank
        String name,
        @NotBlank
        String address,
        @NotNull @Email
        String email,
        @NotNull @Pattern(regexp = "[0-9]{11}")
        String phone,
        List<CardCreateOrUpdateRecord> paymentsList,
        List<VehicleCreateRecord> vehicles
) {}
