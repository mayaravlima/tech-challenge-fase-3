package com.postech.fase3parquimetro.parking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "parking")
@ToString
public class ParkingEntity implements Serializable {

    @Id
    private String id;
    private ParkingType parkingType;
    private int durationInMinutes;
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresIn;

    @DBRef
    private PaymentEntity payedWith;
    private boolean isExtendActive;
    private StatusEnum status;

    public static ParkingEntity from(ParkingCreateOrUpdateRecord parking) {
        final var parkingEntity = ParkingEntity.builder()
                .createdAt(LocalDateTime.now())
                .parkingType(ParkingType.valueOf(parking.parkingType().toUpperCase(Locale.ROOT)))
                .payedWith(parking.payedWith())
                .updatedAt(LocalDateTime.now())
                .status(StatusEnum.ACTIVE)
                .build();

        final var allowAutomaticExtension = parkingEntity.getParkingType() == ParkingType.DURATION;

        parkingEntity.setDurationInMinutes(parking.durationInMinutes());

        if (parking.parkingType().equals(ParkingType.DURATION.toString())) {
            parkingEntity.setDurationInMinutes(60);

        }

        final var expirationTime = parkingEntity.getCreatedAt()
                .plusMinutes(parkingEntity.getDurationInMinutes());

        final var getAmountToPay = BigDecimal.valueOf(parkingEntity.getDurationInMinutes() * 0.10);

        parkingEntity.setExpiresIn(expirationTime);
        parkingEntity.setExtendActive(allowAutomaticExtension);
        parkingEntity.setPrice(getAmountToPay);

        return parkingEntity;
    }

    public void extendPeriod() {
        final int oneHourExtension = 60;

        this.durationInMinutes = oneHourExtension;
        this.expiresIn = this.expiresIn.plusMinutes(oneHourExtension);
        this.price = this.price.add(BigDecimal.valueOf(oneHourExtension * 0.10));
        this.updatedAt = LocalDateTime.now();
        this.status = StatusEnum.ACTIVE;
    }

    public void disableAutomaticExtension() {
        this.isExtendActive = false;
    }
}
