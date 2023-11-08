package com.postech.fase3parquimetro.vehicle.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vehicle")
@ToString
public class VehicleEntity implements Serializable {

    @Id
    private String id;
    private String plate;
    private String brand;
    private String model;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DBRef
    private ParkingEntity parking;

    public static VehicleEntity from(VehicleCreateRecord vehicleCreate) {
        return VehicleEntity.builder()
                .plate(vehicleCreate.plate())
                .brand(vehicleCreate.brand())
                .model(vehicleCreate.model())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void addParkingTiming(ParkingEntity parkingTiming) {
        this.parking = parkingTiming;
    }
}