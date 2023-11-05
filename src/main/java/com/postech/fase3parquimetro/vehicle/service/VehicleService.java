package com.postech.fase3parquimetro.vehicle.service;

import com.postech.fase3parquimetro.parking.exceptions.ParkingException;
import com.postech.fase3parquimetro.parking.model.ParkingCreateOrUpdateRecord;
import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import com.postech.fase3parquimetro.parking.model.ParkingType;
import com.postech.fase3parquimetro.parking.service.ParkingService;
import com.postech.fase3parquimetro.payments.model.PaymentTypeEnum;
import com.postech.fase3parquimetro.vehicle.exceptions.VehicleException;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import com.postech.fase3parquimetro.vehicle.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private ParkingService parkingService;

    public VehicleEntity createVehicle(VehicleCreateRecord vehicle) {
        VehicleEntity vehicleEntity = VehicleEntity.from(vehicle);
        return vehicleRepository.save(vehicleEntity);
    }

    @Cacheable(value = "vehicles", key="#id", condition = "#id != null")
    public VehicleEntity getVehicleByPlate(String plate) {
        return vehicleRepository.findByPlate(plate).orElse(null);
    }

    @Cacheable(value = "vehicles", key="#id",condition = "#id != null")
    public VehicleEntity getVehicleById(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public VehicleEntity addParkingTiming(String id, ParkingCreateOrUpdateRecord parkingTiming) {
        final var vehicleEntity = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException("Vehicle not found", HttpStatus.NOT_FOUND.value()));

        if (checkIfPaymentIsNotAllowed(parkingTiming)) {
            throw new ParkingException("PIX is not allowed for non-fixed parking time", HttpStatus.BAD_REQUEST.value());
        }

        final var parkingEntity = parkingService.save(parkingTiming);

        vehicleEntity.addParkingTiming(parkingEntity);

        return vehicleRepository.save(vehicleEntity);
    }

    private boolean checkIfPaymentIsNotAllowed(ParkingCreateOrUpdateRecord parkingRequest) {
        return parkingRequest.payedWith().getPaymentType() == PaymentTypeEnum.PIX
                && ParkingType.valueOf(parkingRequest.parkingType().toUpperCase(Locale.ROOT)) == ParkingType.DURATION;
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }

}