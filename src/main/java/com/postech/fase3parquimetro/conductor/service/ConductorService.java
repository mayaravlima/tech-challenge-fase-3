package com.postech.fase3parquimetro.conductor.service;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.conductor.model.ConductorCreateOrUpdateRecord;
import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.conductor.repository.ConductorRepository;
import com.postech.fase3parquimetro.payments.model.CardCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.payments.service.PaymentService;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import com.postech.fase3parquimetro.vehicle.service.VehicleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;

    private final VehicleService vehicleService;
    private final PaymentService paymentService;

    @Transactional
    public ConductorEntity createConductor(ConductorCreateOrUpdateRecord conductor) {
        ConductorEntity conductorEntity = ConductorEntity.from(conductor);
        verifyIfEmailAlreadyExists(conductor.email());
        verifyIfPhoneAlreadyExists(conductor.phone());
        conductorEntity.setCreatedAt(LocalDateTime.now());

        List<VehicleEntity> vehicles = new ArrayList<>();

        if (Objects.nonNull(conductor.vehicles())) {
            conductor.vehicles().forEach(vehicle -> {
                final var vehicleByPlate = vehicleService.getVehicleByPlate(vehicle.plate());

                if (vehicleByPlate != null) {
                    throw new ConductorException("Vehicle already exists", HttpStatus.BAD_REQUEST.value());
                }

                VehicleEntity newVehicle = vehicleService.createVehicle(vehicle);

                log.info("Vehicle created: {} - {}", newVehicle.getModel(), newVehicle.getPlate());

                vehicles.add(newVehicle);

            });
        }

        conductorEntity.setVehicles(vehicles);

        List<PaymentEntity> cardList = new ArrayList<>();

        if (Objects.nonNull(conductor.paymentsList())) {
            conductor.paymentsList().forEach(payment -> {
                final var paymentEntity = paymentService.save(payment);

                log.info("Payment created: {} - {}", paymentEntity.getCardNumber(), paymentEntity.getCardHolder());

                cardList.add(paymentEntity);
            });
        }

        conductorEntity.setCardList(cardList);

        return conductorRepository.save(conductorEntity);
    }

    public ConductorEntity addVehicleToConductor(String conductorId, VehicleCreateRecord vehicle) {
        final var conductor = getConductorById(conductorId);
        final var vehicleEntity = vehicleService.createVehicle(vehicle);

        conductor.addVehicleToList(vehicleEntity);

        return conductorRepository.save(conductor);
    }

    public ConductorEntity addCardToConductor(String conductorId, CardCreateOrUpdateRecord payment) {
        final var conductor = getConductorById(conductorId);
        final var paymentEntity = paymentService.save(payment);

        conductor.addCardToList(paymentEntity);

        return conductorRepository.save(conductor);
    }

    public ConductorEntity getConductorById(String id) {
        return conductorRepository.findById(id).orElseThrow(() -> new ConductorException("Conductor not found", HttpStatus.NOT_FOUND.value()));
    }


    public List<ConductorEntity> getAllConductors() {
        return conductorRepository.findAll();
    }

    private void verifyIfEmailAlreadyExists(String email) {
        conductorRepository.findByEmail(email).ifPresent(c -> {
            throw new ConductorException("Email already exists", HttpStatus.BAD_REQUEST.value());
        });
    }

    private void verifyIfPhoneAlreadyExists(String phone) {
        conductorRepository.findByPhone(phone).ifPresent(c -> {
            throw new ConductorException("Phone already exists", HttpStatus.BAD_REQUEST.value());
        });
    }
}