package com.postech.fase3parquimetro.vehicle.service;

import com.postech.fase3parquimetro.conductor.exceptions.ConductorException;
import com.postech.fase3parquimetro.parking.exceptions.ParkingException;
import com.postech.fase3parquimetro.parking.model.ParkingCreateOrUpdateRecord;
import com.postech.fase3parquimetro.parking.model.ParkingType;
import com.postech.fase3parquimetro.parking.model.StatusEnum;
import com.postech.fase3parquimetro.parking.service.ParkingService;
import com.postech.fase3parquimetro.payments.exceptions.PaymentException;
import com.postech.fase3parquimetro.payments.model.PaymentEntity;
import com.postech.fase3parquimetro.payments.model.PaymentTypeEnum;
import com.postech.fase3parquimetro.payments.service.PaymentService;
import com.postech.fase3parquimetro.vehicle.exceptions.VehicleException;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import com.postech.fase3parquimetro.vehicle.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private ParkingService parkingService;
    private PaymentService paymentService;

    @Transactional
    public VehicleEntity createVehicle(VehicleCreateRecord vehicle) {
        VehicleEntity vehicleEntity = VehicleEntity.from(vehicle);
        return vehicleRepository.save(vehicleEntity);
    }


    public VehicleEntity getVehicleById(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public VehicleEntity addParkingTiming(String id, ParkingCreateOrUpdateRecord parkingTiming) {
        final var vehicleEntity = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException("Vehicle not found", HttpStatus.NOT_FOUND.value()));

        final var paymentMethod = paymentService.getPaymentById(parkingTiming.payedWith().getId());

        if (paymentMethod == null) {
            throw new PaymentException("Payment method not found", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.nonNull(vehicleEntity.getParking())) {
            if (vehicleEntity.getParking().getStatus().equals(StatusEnum.ACTIVE)
            || vehicleEntity.getParking().getStatus().equals(StatusEnum.NEAR_EXPIRATION)) {
                throw new ParkingException("Vehicle already has an active parking", HttpStatus.BAD_REQUEST.value());
            }
        }
        if (checkIfPaymentIsNotAllowed(parkingTiming, paymentMethod)) {
            throw new ParkingException("PIX is not allowed for non-fixed parking time", HttpStatus.BAD_REQUEST.value());
        }

        final var parkingEntity = parkingService.save(parkingTiming);

        vehicleEntity.addParkingTiming(parkingEntity);

        return vehicleRepository.save(vehicleEntity);
    }

    private boolean checkIfPaymentIsNotAllowed(ParkingCreateOrUpdateRecord parkingRequest, PaymentEntity paymentEntity) {
        return paymentEntity.getPaymentType() == PaymentTypeEnum.PIX
                && ParkingType.valueOf(parkingRequest.parkingType().toUpperCase(Locale.ROOT)) == ParkingType.DURATION;
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional
    public void deleteVehicle(String id) {
        final var vehicle = getVehicleById(id);
        if (vehicle == null) {
            throw new VehicleException("Vehicle not found", HttpStatus.NOT_FOUND.value());
        }
        vehicleRepository.deleteById(id);
    }

    public void validateListOfVehicles(List<VehicleCreateRecord> vehicleRecords) {

        if (vehicleRecords == null || vehicleRecords.isEmpty()) {
            return;
        }

        HashSet<String> allPlates = new HashSet<>();

        for (VehicleCreateRecord vehicleCreateRecord : vehicleRecords) {
            if (allPlates.contains(vehicleCreateRecord.plate())) {
                throw new ConductorException("Duplicate vehicles for the same driver are not permitted", HttpStatus.BAD_REQUEST.value());
            }
            allPlates.add(vehicleCreateRecord.plate());
        }
    }

    public void disableAutomaticExtension(String id) {
        final var vehicleEntity = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException("Vehicle not found", HttpStatus.NOT_FOUND.value()));

        if(Objects.isNull(vehicleEntity.getParking())
        || vehicleEntity.getParking().getStatus().equals(StatusEnum.EXPIRED)) {
            throw new ParkingException("Vehicle does not have an active parking", HttpStatus.BAD_REQUEST.value());
        }

        final var parking = vehicleEntity.getParking();
        parking.disableAutomaticExtension();

        vehicleEntity.setParking(parking);

        parkingService.update(parking);

        vehicleRepository.save(vehicleEntity);
    }

}