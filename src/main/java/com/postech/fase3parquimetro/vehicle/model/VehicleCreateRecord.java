package com.postech.fase3parquimetro.vehicle.model;

import jakarta.validation.constraints.NotBlank;

public record VehicleCreateRecord(
        @NotBlank(message = "Plate can't be empty or null")
        String plate,
        @NotBlank(message = "Brand can't be empty or null")
        String brand,
        @NotBlank(message = "Model can't be empty or null")
        String model
){}
