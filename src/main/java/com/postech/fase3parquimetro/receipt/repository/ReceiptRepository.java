package com.postech.fase3parquimetro.receipt.repository;

import com.postech.fase3parquimetro.receipt.model.ReceiptEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends MongoRepository<ReceiptEntity, String> {
}
