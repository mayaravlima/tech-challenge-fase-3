package com.postech.fase3parquimetro.conductor.model;

import com.postech.fase3parquimetro.payments.model.PaymentCreateOrUpdateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ConductorCreateRecord(
        @NotNull(message = "Name can't be empty or null")
        @Pattern(regexp = "[a-zA-Z\\s-]+", message = "Name must contain only letters, spaces, or hyphens")
        @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
        String name,
        @NotBlank(message = "Address can't be empty or null")
        String address,
        @NotNull(message = "Email can't be empty or null")
        @Email(message = "Email must be valid")
        String email,
        @NotNull(message = "Phone can't be empty or null")
        @Pattern(regexp = "[0-9]{11}", message = "Phone must contain only 11 digits")
        String phone,
        @Valid
        List<PaymentCreateOrUpdateRecord> paymentsList,
        @Valid
        List<VehicleCreateRecord> vehicles
) {
}
