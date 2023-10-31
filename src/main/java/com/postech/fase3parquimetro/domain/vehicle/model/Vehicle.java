package com.postech.fase3parquimetro.domain.vehicle.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vehicle")
@ToString
public class Vehicle {

    @Id
    private String id;
    private String plate;
    private String brand;
    private String model;
    private LocalDateTime createdAt;

}
