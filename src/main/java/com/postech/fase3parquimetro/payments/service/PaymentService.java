package com.postech.fase3parquimetro.payments.service;

import com.postech.fase3parquimetro.payments.model.CardCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentEntity save(CardCreateOrUpdateRecord paymentRequest) {
        final var paymentEntity = PaymentEntity.from(paymentRequest);
        return paymentRepository.save(paymentEntity);
    }
}
