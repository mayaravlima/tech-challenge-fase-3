package com.postech.fase3parquimetro.conductor.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ConductorUpdateRecord(

        @Pattern(regexp = "[a-zA-Z\\s-]+", message = "Name must contain only letters, spaces, or hyphens")
        @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
        String name,

        String address,

        @Email(message = "Email must be valid")
        String email,

        @Pattern(regexp = "[0-9]{11}", message = "Phone must contain only 11 digits")
        String phone
) {
}
