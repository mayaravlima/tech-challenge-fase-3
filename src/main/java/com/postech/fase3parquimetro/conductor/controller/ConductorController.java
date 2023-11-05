package com.postech.fase3parquimetro.conductor.controller;

import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.conductor.service.ConductorService;
import com.postech.fase3parquimetro.conductor.model.ConductorCreateOrUpdateRecord;
import com.postech.fase3parquimetro.payments.model.CardCreateOrUpdateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conductor")
@AllArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @PostMapping
    public ResponseEntity<ConductorEntity> createConductor(@RequestBody @Valid ConductorCreateOrUpdateRecord conductor) {
        ConductorEntity createdConductor = conductorService.createConductor(conductor);
        return new ResponseEntity<>(createdConductor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/vehicle")
    public ResponseEntity<ConductorEntity> addVehicleToConductor(@PathVariable String id, @RequestBody @Valid VehicleCreateRecord vehicle) {
        ConductorEntity updatedConductor = conductorService.addVehicleToConductor(id, vehicle);
        return new ResponseEntity<>(updatedConductor, HttpStatus.OK);
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<ConductorEntity> addPaymentToConductor(@PathVariable String id, @RequestBody @Valid CardCreateOrUpdateRecord payment) {
        ConductorEntity updatedConductor = conductorService.addCardToConductor(id, payment);
        return new ResponseEntity<>(updatedConductor, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorEntity> getConductor(@PathVariable String id) {
        ConductorEntity conductor = conductorService.getConductorById(id);
        if (conductor != null) {
            return new ResponseEntity<>(conductor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ConductorEntity>> getAllConductors() {
        List<ConductorEntity> conductors = conductorService.getAllConductors();
        return new ResponseEntity<>(conductors, HttpStatus.OK);
    }

}