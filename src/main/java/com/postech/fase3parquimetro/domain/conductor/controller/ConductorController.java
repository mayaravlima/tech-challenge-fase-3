package com.postech.fase3parquimetro.domain.conductor.controller;

import com.postech.fase3parquimetro.domain.conductor.model.Conductor;
import com.postech.fase3parquimetro.domain.conductor.service.ConductorService;
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
    public ResponseEntity<Conductor> createConductor(@RequestBody Conductor conductor) {
        Conductor createdConductor = conductorService.createConductor(conductor);
        return new ResponseEntity<>(createdConductor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conductor> getConductor(@PathVariable String id) {
        Conductor conductor = conductorService.getConductorById(id);
        if (conductor != null) {
            return new ResponseEntity<>(conductor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Conductor>> getAllConductors() {
        List<Conductor> conductors = conductorService.getAllConductors();
        return new ResponseEntity<>(conductors, HttpStatus.OK);
    }

}
