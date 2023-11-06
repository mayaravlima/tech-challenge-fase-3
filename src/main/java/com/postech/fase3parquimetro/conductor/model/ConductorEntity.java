package com.postech.fase3parquimetro.conductor.model;

import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conductor")
@ToString
public class ConductorEntity {

    @Id
    private String id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private LocalDateTime createdAt;

    @DBRef
    private List<PaymentEntity> payments;
    @DBRef
    private List<VehicleEntity> vehicles;

    public static ConductorEntity from(ConductorCreateOrUpdateRecord conductor) {
        return ConductorEntity.builder()
                .name(conductor.name())
                .address(conductor.address())
                .email(conductor.email())
                .phone(conductor.phone())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void addVehicleToList(VehicleEntity vehicleEntity) {
        this.vehicles.add(vehicleEntity);
    }

    public void addCardToList(PaymentEntity paymentEntity) {
        this.payments.add(paymentEntity);
    }
}