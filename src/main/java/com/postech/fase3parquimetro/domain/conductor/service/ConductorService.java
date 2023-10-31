package com.postech.fase3parquimetro.domain.conductor.service;

import com.postech.fase3parquimetro.domain.conductor.model.Conductor;
import com.postech.fase3parquimetro.domain.conductor.repository.ConductorRepository;
import com.postech.fase3parquimetro.domain.vehicle.model.Vehicle;
import com.postech.fase3parquimetro.domain.vehicle.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ConductorService {

    private ConductorRepository conductorRepository;

    private VehicleService vehicleService;

    @Transactional
    public Conductor createConductor(Conductor conductor) {
        conductor.setCreatedAt(LocalDateTime.now());

        List<Vehicle> vehicles = new ArrayList<>();

        if(conductor.getVehicles() != null) {
            conductor.getVehicles().forEach(vehicle -> {

                if(vehicle.getId() == null) {
                    Vehicle newVehicle = vehicleService.createVehicle(vehicle);
                    System.out.println(newVehicle.toString());
                    vehicles.add(newVehicle);
                }

            });
        }
        conductor.setVehicles(vehicles);
        System.out.println(conductor.toString());
        return conductorRepository.save(conductor);
    }

    public Conductor getConductorById(String id) {
        return conductorRepository.findById(id).orElse(null);
    }

    public List<Conductor> getAllConductors() {
        return conductorRepository.findAll();
    }
}
