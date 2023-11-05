package com.postech.fase3parquimetro.receipt.service;

import com.postech.fase3parquimetro.conductor.repository.ConductorRepository;
import com.postech.fase3parquimetro.conductor.service.ConductorService;
import com.postech.fase3parquimetro.parking.repository.ParkingRepository;
import com.postech.fase3parquimetro.payments.repository.PaymentRepository;
import com.postech.fase3parquimetro.receipt.ReceiptException;
import com.postech.fase3parquimetro.receipt.model.ReceiptEntity;
import com.postech.fase3parquimetro.receipt.model.ReceiptReadRecord;
import com.postech.fase3parquimetro.receipt.repository.ReceiptRepository;
import com.postech.fase3parquimetro.vehicle.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ConductorRepository conductorRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;

    public ReceiptReadRecord getById(String id) {
        return receiptRepository.findById(id).map(ReceiptReadRecord::with).orElseThrow(() ->
                new ReceiptException("Receipt not found for given id", HttpStatus.NOT_FOUND.value()));
    }

    public ReceiptEntity save(ReceiptEntity receiptEntity) {
        final var vehicleEntity = vehicleRepository.findByParkingId(receiptEntity.getParking().getId());
        final var paymentEntity = paymentRepository.findByParkingId(receiptEntity.getParking().getId());
        final var conductorEntity = conductorRepository.findByVehicleId(vehicleEntity.getId());

        receiptEntity.setConductor(conductorEntity);
        receiptEntity.setVehicle(vehicleEntity);
        receiptEntity.setPayment(paymentEntity);

        return receiptRepository.save(receiptEntity);
    }

    public List<ReceiptEntity> findAllReceipts() {
        return receiptRepository.findAll();
    }
}
