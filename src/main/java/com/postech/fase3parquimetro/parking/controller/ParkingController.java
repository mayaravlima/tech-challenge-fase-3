package com.postech.fase3parquimetro.parking.controller;

import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import com.postech.fase3parquimetro.parking.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parking")
@AllArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @GetMapping
    public List<ParkingEntity> getAllParkings() {
        return parkingService.getAllParkings();
    }
}
