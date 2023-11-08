package com.postech.fase3parquimetro.conductor.service;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.conductor.model.ConductorCreateRecord;
import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.conductor.model.ConductorUpdateRecord;
import com.postech.fase3parquimetro.conductor.repository.ConductorRepository;
import com.postech.fase3parquimetro.payments.model.PaymentCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.payments.model.PaymentTypeEnum;
import com.postech.fase3parquimetro.payments.service.PaymentService;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import com.postech.fase3parquimetro.vehicle.service.VehicleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final VehicleService vehicleService;
    private final PaymentService paymentService;

    @Transactional
    @CacheEvict(value = {"conductor", "conductor:allConductors"}, allEntries = true)
    public ConductorEntity createConductor(ConductorCreateRecord conductor) {
        ConductorEntity conductorEntity = ConductorEntity.from(conductor);

        verifyIfEmailAlreadyExists(conductor.email());
        verifyIfPhoneAlreadyExists(conductor.phone());

        conductorEntity.setCreatedAt(LocalDateTime.now());

        vehicleService.validateListOfVehicles(conductor.vehicles());
        paymentService.validatePayment(conductor.paymentsList());

        List<VehicleEntity> vehicles = createVehicles(conductor.vehicles());
        List<PaymentEntity> cardList = createPayments(conductor.paymentsList());

        conductorEntity.setVehicles(vehicles);
        conductorEntity.setPayments(cardList);

        return conductorRepository.save(conductorEntity);
    }

    private List<VehicleEntity> createVehicles(List<VehicleCreateRecord> vehicleRecords) {

        return vehicleRecords.stream()
                .map(vehicle -> {
                    VehicleEntity newVehicle = vehicleService.createVehicle(vehicle);
                    log.info("Vehicle created: {} - {}", newVehicle.getModel(), newVehicle.getPlate());
                    return newVehicle;
                })
                .collect(Collectors.toList());
    }

    private List<PaymentEntity> createPayments(List<PaymentCreateOrUpdateRecord> paymentRecords) {
        return paymentRecords.stream()
                .map(payment -> {
                    PaymentEntity paymentEntity = paymentService.save(payment);
                    log.info("Payment created: {} - {}", paymentEntity.getCardNumber(), paymentEntity.getCardHolder());
                    return paymentEntity;
                })
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"conductor", "conductor:allConductors"}, allEntries = true)
    public ConductorEntity addVehicleToConductor(String conductorId, VehicleCreateRecord vehicle) {
        final var conductor = getConductorById(conductorId);

        if (Objects.nonNull(conductor.getVehicles())) {
            for (VehicleEntity item : conductor.getVehicles()) {
                if (item.getPlate().equals(vehicle.plate())) {
                    throw new ConductorException("Vehicle already exists", HttpStatus.BAD_REQUEST.value());
                }
            }
        }
        final var vehicleEntity = vehicleService.createVehicle(vehicle);

        conductor.addVehicleToList(vehicleEntity);

        return conductorRepository.save(conductor);
    }

    @CacheEvict(value = {"conductor", "conductor:allConductors"}, allEntries = true)
    public ConductorEntity addPaymentToConductor(String conductorId, PaymentCreateOrUpdateRecord payment) {
        final var conductor = getConductorById(conductorId);

        paymentService.validateDataOfPayment(payment);

        if (Objects.nonNull(conductor.getPayments())) {
            for (PaymentEntity item : conductor.getPayments()) {
                if (!item.getPaymentType().equals(PaymentTypeEnum.PIX) &&
                        item.getCardNumber().equals(payment.cardNumber())
                        && item.getPaymentType().equals(payment.paymentType())) {
                    throw new ConductorException("Card already exists", HttpStatus.BAD_REQUEST.value());
                }

                if (item.isFavorite() && payment.isFavorite()) {
                    throw new ConductorException("Only one favorite card is allowed", HttpStatus.BAD_REQUEST.value());
                }

                if (item.getPaymentType().equals(PaymentTypeEnum.PIX) && payment.paymentType().equals(PaymentTypeEnum.PIX)) {
                    throw new ConductorException("Only one PIX is allowed", HttpStatus.BAD_REQUEST.value());
                }
            }
        }
        final var paymentEntity = paymentService.save(payment);

        conductor.addCardToList(paymentEntity);

        return conductorRepository.save(conductor);
    }

    @Cacheable(key = "#id", value = "conductor", unless = "#result == null")
    public ConductorEntity getConductorById(String id) {
        return conductorRepository.findById(id).orElseThrow(() -> new ConductorException("Conductor not found", HttpStatus.NOT_FOUND.value()));
    }

    @Cacheable(value = "conductor:allConductors", unless = "#result.size() == 0")
    public List<ConductorEntity> getAllConductors() {
        return conductorRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = {"conductor", "conductor:allConductors", "conductor:byEmail", "conductor:byPhone"}, allEntries = true)
    public void deleteConductor(String id) {
        final var conductor = getConductorById(id);

        if (Objects.nonNull(conductor.getVehicles())) {
            conductor.getVehicles().forEach(vehicle -> vehicleService.deleteVehicle(vehicle.getId()));
        }

        if (Objects.nonNull(conductor.getPayments())) {
            conductor.getPayments().forEach(payment -> paymentService.deletePayment(payment.getId()));
        }
        conductorRepository.deleteById(id);
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

    @CacheEvict(value = {"conductor", "conductor:allConductors", "conductor:byEmail", "conductor:byPhone"}, allEntries = true)
    public ConductorEntity updateConductor(String id, ConductorUpdateRecord conductor) {
        final var conductorEntity = getConductorById(id);

        if (Objects.nonNull(conductor.name())) {
            conductorEntity.setName(conductor.name());
        }

        if (Objects.nonNull(conductor.address())) {
            conductorEntity.setAddress(conductor.address());
        }

        if (Objects.nonNull(conductor.email())) {
            verifyIfEmailAlreadyExists(conductor.email());
            conductorEntity.setEmail(conductor.email());
        }

        if (Objects.nonNull(conductor.phone())) {
            verifyIfPhoneAlreadyExists(conductor.phone());
            conductorEntity.setPhone(conductor.phone());
        }

        return conductorRepository.save(conductorEntity);
    }

    @Transactional
    @CacheEvict(value = {"conductor", "conductor:allConductors"}, allEntries = true)
    public void removingVehicleFromConductor(String vehicleId) {
        final var vehicle = vehicleService.getVehicleById(vehicleId);

        if (vehicle == null) {
            throw new ConductorException("Vehicle not found", HttpStatus.NOT_FOUND.value());
        }

        final var conductor = conductorRepository.findByVehiclesId(vehicle.getId());
        if (conductor != null) {
            conductor.getVehicles().removeIf(item -> Objects.equals(item.getId(), vehicle.getId()));
            conductorRepository.save(conductor);
        }

        vehicleService.deleteVehicle(vehicleId);
    }

    @CacheEvict(value = {"conductor", "conductor:allConductors"}, allEntries = true)
    public void removingPaymentFromConductor(String id) {
        final var payment = paymentService.getPaymentById(id);

        if (payment == null) {
            throw new ConductorException("Payment not found", HttpStatus.NOT_FOUND.value());
        }

        final var conductor = conductorRepository.findByPaymentsId(payment.getId());
        if (conductor != null) {
            conductor.getPayments().removeIf(item -> Objects.equals(item.getId(), payment.getId()));
            conductorRepository.save(conductor);
        }

        paymentService.deletePayment(id);
    }

}