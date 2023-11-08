package com.postech.fase3parquimetro.payments.repository;

import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
}
