package com.postech.fase3parquimetro.payments.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment")
@ToString
public class PaymentEntity {

    @Id
    private String id;
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cardCvv;
    private PaymentTypeEnum paymentType;
    private boolean isFavorite;

    public static PaymentEntity from(PaymentCreateOrUpdateRecord paymentEntity) {

        return PaymentEntity.builder()
                .cardNumber(paymentEntity.cardNumber())
                .cardHolder(paymentEntity.cardHolder())
                .expirationDate(paymentEntity.expirationDate())
                .cardCvv(paymentEntity.cardCvv())
                .paymentType(paymentEntity.paymentType())
                .isFavorite(paymentEntity.isFavorite())
                .build();
    }

    public static PaymentEntity fromPixType(PaymentCreateOrUpdateRecord paymentEntity) {
        return PaymentEntity.builder()
                .cardHolder(paymentEntity.cardHolder())
                .expirationDate("60")
                .paymentType(PaymentTypeEnum.PIX)
                .isFavorite(paymentEntity.isFavorite())
                .build();
    }
}
