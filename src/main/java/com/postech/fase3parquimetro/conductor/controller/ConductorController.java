package com.postech.fase3parquimetro.conductor.controller;

import com.postech.fase3parquimetro.conductor.model.ConductorCreateRecord;
import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import com.postech.fase3parquimetro.conductor.model.ConductorUpdateRecord;
import com.postech.fase3parquimetro.conductor.service.ConductorService;
import com.postech.fase3parquimetro.payments.model.PaymentCreateOrUpdateRecord;
import com.postech.fase3parquimetro.vehicle.model.VehicleCreateRecord;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conductor")
@AllArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @PostMapping
    public ResponseEntity<ConductorEntity> createConductor(@RequestBody @Valid ConductorCreateRecord conductor) {
        ConductorEntity createdConductor = conductorService.createConductor(conductor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConductor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorEntity> updateConductor(@PathVariable String id, @RequestBody @Valid ConductorUpdateRecord conductor) {
        ConductorEntity updatedConductor = conductorService.updateConductor(id, conductor);
        return ResponseEntity.ok(updatedConductor);
    }

    @PutMapping("/{id}/vehicle")
    public ResponseEntity<ConductorEntity> addVehicleToConductor(@PathVariable String id, @RequestBody @Valid VehicleCreateRecord vehicle) {
        ConductorEntity updatedConductor = conductorService.addVehicleToConductor(id, vehicle);
        return ResponseEntity.status(HttpStatus.OK).body(updatedConductor);
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<ConductorEntity> addPaymentToConductor(@PathVariable String id, @RequestBody @Valid PaymentCreateOrUpdateRecord payment) {
        ConductorEntity updatedConductor = conductorService.addPaymentToConductor(id, payment);
        return ResponseEntity.ok(updatedConductor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorEntity> getConductor(@PathVariable String id) {
        ConductorEntity conductor = conductorService.getConductorById(id);
        return ResponseEntity.ok(conductor);
    }

    @GetMapping
    public ResponseEntity<List<ConductorEntity>> getAllConductors() {
        List<ConductorEntity> conductors = conductorService.getAllConductors();
        return ResponseEntity.ok(conductors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteConductor(@PathVariable String id) {
        conductorService.deleteConductor(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Conductor deleted successfully"));
    }

    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<Map<String, String>> deleteVehicleFromConductor(@PathVariable String id) {
        conductorService.removingVehicleFromConductor(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Vehicle deleted from conductor successfully"));
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<Map<String, String>> deletePaymentFromConductor(@PathVariable String id) {
        conductorService.removingPaymentFromConductor(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Payment deleted from conductor successfully"));
    }

}