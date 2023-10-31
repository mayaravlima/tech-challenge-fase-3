package com.postech.fase3parquimetro.domain.vehicle.service;

import com.postech.fase3parquimetro.domain.vehicle.model.Vehicle;
import com.postech.fase3parquimetro.domain.vehicle.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VehicleService {

    private VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setCreatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

}
