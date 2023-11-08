package com.postech.fase3parquimetro.receipt.model;

import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "receipt")
@ToString
public class ReceiptEntity {

    @Id
    private String id;
    @DBRef
    private ConductorEntity conductor;
    @DBRef
    private VehicleEntity vehicle;
    @DBRef
    private PaymentEntity payment;
    @DBRef
    private ParkingEntity parking;
    private LocalDateTime createdAt;
}
