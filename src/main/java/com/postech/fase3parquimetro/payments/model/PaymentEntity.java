package com.postech.fase3parquimetro.payments.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment")
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentEntity implements Serializable {

    @Id
    private String id;
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cardCvv;
    private PaymentTypeEnum paymentType;
    @JsonProperty
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
                .paymentType(PaymentTypeEnum.PIX)
                .isFavorite(paymentEntity.isFavorite())
                .build();
    }
}
