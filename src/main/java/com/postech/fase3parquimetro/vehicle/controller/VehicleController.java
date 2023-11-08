package com.postech.fase3parquimetro.vehicle.controller;

import com.postech.fase3parquimetro.parking.model.ParkingCreateOrUpdateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import com.postech.fase3parquimetro.vehicle.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicle")
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleEntity> createVehicle(@RequestBody VehicleCreateRecord vehicle) {
        VehicleEntity createdVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleEntity> getVehicle(@PathVariable String id) {
        VehicleEntity vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            return ResponseEntity.status( HttpStatus.OK).body(vehicle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleEntity> addParkingTiming(@PathVariable String id, @RequestBody ParkingCreateOrUpdateRecord parkingTiming) {
        VehicleEntity updatedVehicle = vehicleService.addParkingTiming(id, parkingTiming);
        return ResponseEntity.status(HttpStatus.OK).body(updatedVehicle);
    }

    @PutMapping("/disableAutomaticExtension/{id}")
    public ResponseEntity<Map<String, String>> disableAutomaticExtension(@PathVariable String id) {
        vehicleService.disableAutomaticExtension(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Automatic extension disabled"));
    }

    @GetMapping
    public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
        List<VehicleEntity> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.status(HttpStatus.OK).body(vehicles);
    }

}