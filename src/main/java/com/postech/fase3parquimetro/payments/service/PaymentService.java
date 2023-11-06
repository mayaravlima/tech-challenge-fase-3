package com.postech.fase3parquimetro.payments.service;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.payments.exceptions.PaymentException;
import com.postech.fase3parquimetro.payments.model.PaymentCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.payments.model.PaymentTypeEnum;
import com.postech.fase3parquimetro.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentEntity save(PaymentCreateOrUpdateRecord paymentRequest) {

        if (paymentRequest.paymentType().equals(PaymentTypeEnum.PIX)) {
            final var paymentEntity = PaymentEntity.fromPixType(paymentRequest);
            return paymentRepository.save(paymentEntity);
        }
        final var paymentEntity = PaymentEntity.from(paymentRequest);
        return paymentRepository.save(paymentEntity);
    }

    public PaymentEntity getPaymentById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public void validatePayment(List<PaymentCreateOrUpdateRecord> paymentRecords) {
        if (paymentRecords == null || paymentRecords.isEmpty()) {
            return;
        }

        validateDuplicatePayments(paymentRecords);
        validateDataOfListPayments(paymentRecords);
    }

    public void validateDataOfPayment(PaymentCreateOrUpdateRecord paymentRecord) {
        if (paymentRecord == null) {
            return;
        }

        if (paymentRecord.paymentType() == null) {
            throw new PaymentException("Payment type is required", HttpStatus.BAD_REQUEST.value());
        }

        validateDataOfListPayments(List.of(paymentRecord));
    }

    private void validateDataOfListPayments(List<PaymentCreateOrUpdateRecord> paymentRecords) {

        for (PaymentCreateOrUpdateRecord paymentEntity : paymentRecords) {

            if (!paymentEntity.paymentType().equals(PaymentTypeEnum.PIX)) {
                validateRequiredField(paymentEntity.cardNumber(), "Card number is required");
                validateRequiredField(paymentEntity.cardHolder(), "Card holder is required");
                validateRequiredField(paymentEntity.expirationDate(), "Expiration date is required");
                validateRequiredField(paymentEntity.cardCvv(), "Card CVV is required");
                validateRequiredField(paymentEntity.expirationDate(), "Expiration date is required");
            }
        }
    }

    private static void validateRequiredField(String value, String errorMessage) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new PaymentException(errorMessage, HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateDuplicatePayments(List<PaymentCreateOrUpdateRecord> paymentRecords) {
        long favoriteCount = paymentRecords.stream()
                .filter(PaymentCreateOrUpdateRecord::isFavorite)
                .count();

        if (favoriteCount == 0 || favoriteCount > 1) {
            throw new ConductorException("One payment method must be favorite", HttpStatus.BAD_REQUEST.value());
        }

        long pixCount = paymentRecords.stream()
                .filter(payment -> PaymentTypeEnum.PIX.equals(payment.paymentType()))
                .count();

        if (pixCount > 1) {
            throw new ConductorException("Only one PIX is allowed", HttpStatus.BAD_REQUEST.value());
        }

        Set<String> uniqueCardNumberPaymentTypes = new HashSet<>();

        for (PaymentCreateOrUpdateRecord paymentCreateOrUpdateRecord : paymentRecords) {
            String key = paymentCreateOrUpdateRecord.cardNumber() + "-" + paymentCreateOrUpdateRecord.paymentType();
            if (!uniqueCardNumberPaymentTypes.add(key)) {
                throw new ConductorException("Duplicate cards for the same driver are not permitted", HttpStatus.BAD_REQUEST.value());
            }
        }
    }

    public void deletePayment(String id) {
        final var payment = getPaymentById(id);
        if (payment == null) {
            throw new PaymentException("Payment not found", HttpStatus.NOT_FOUND.value());
        }
        paymentRepository.deleteById(id);
    }
}
