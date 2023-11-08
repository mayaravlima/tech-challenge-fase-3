package com.postech.fase3parquimetro.receipt.service;

import com.postech.fase3parquimetro.conductor.repository.ConductorRepository;
import com.postech.fase3parquimetro.payments.repository.PaymentRepository;
import com.postech.fase3parquimetro.receipt.ReceiptException;
import com.postech.fase3parquimetro.receipt.model.ReceiptEntity;
import com.postech.fase3parquimetro.receipt.model.ReceiptReadRecord;
import com.postech.fase3parquimetro.receipt.repository.ReceiptRepository;
import com.postech.fase3parquimetro.vehicle.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ConductorRepository conductorRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;

    @Cacheable(key = "#id", value = "receipt", unless = "#result == null")
    public ReceiptReadRecord getById(String id) {
        return receiptRepository.findById(id).map(ReceiptReadRecord::with).orElseThrow(() ->
                new ReceiptException("Receipt not found for given id", HttpStatus.NOT_FOUND.value()));
    }

    @CacheEvict(value = {"receipt"}, allEntries = true)
    public ReceiptEntity save(ReceiptEntity receiptEntity) {
        final var vehicleEntity = vehicleRepository.findByParkingId(receiptEntity.getParking().getId());
        final var paymentEntity = paymentRepository.findById(receiptEntity.getParking().getPayedWith().getId()).get();
        final var conductorEntity = conductorRepository.findByVehiclesId(vehicleEntity.getId());

        log.info("Payment {}", paymentEntity.getId());

        receiptEntity.setConductor(conductorEntity);
        receiptEntity.setVehicle(vehicleEntity);
        receiptEntity.setPayment(paymentEntity);

        return receiptRepository.save(receiptEntity);
    }

    @Cacheable(value = "receipt:allValues", unless = "#result.size() == 0")
    public List<ReceiptReadRecord> findAllReceipts() {
        return receiptRepository.findAll().stream().map(ReceiptReadRecord::with).collect(Collectors.toList());
    }
}
