package com.postech.fase3parquimetro.domain.conductor.model;

import com.postech.fase3parquimetro.domain.paymentMethod.enums.PaymentMethod;
import com.postech.fase3parquimetro.domain.vehicle.model.Vehicle;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conductor")
@ToString
public class Conductor {

    @Id
    private String id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private PaymentMethod favoritePaymentMethod;
    private LocalDateTime createdAt;

    @DBRef
    private List<Vehicle> vehicles;
}
