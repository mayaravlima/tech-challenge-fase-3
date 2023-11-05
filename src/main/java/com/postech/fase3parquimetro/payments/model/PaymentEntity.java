package com.postech.fase3parquimetro.payments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;
import java.util.Objects;

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

    public static PaymentEntity from(CardCreateOrUpdateRecord paymentEntity) {
        final var favoriteMethod = paymentEntity.isFavorite();

        return PaymentEntity.builder()
                .cardNumber(paymentEntity.cardNumber())
                .cardHolder(paymentEntity.cardHolder())
                .expirationDate(paymentEntity.expirationDate())
                .cardCvv(paymentEntity.cardCvv())
                .paymentType(PaymentTypeEnum.valueOf(paymentEntity.cardType().toUpperCase(Locale.ROOT)))
                .isFavorite(favoriteMethod)
                .build();
    }

    public static PaymentEntity from(PixCreateRecord pixCreateRecord) {
        return PaymentEntity.builder()
                .cardHolder(pixCreateRecord.payerName())
                .expirationDate("60")
                .paymentType(PaymentTypeEnum.PIX)
                .build();
    }
}
