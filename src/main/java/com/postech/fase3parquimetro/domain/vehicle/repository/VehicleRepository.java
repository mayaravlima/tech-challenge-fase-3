package com.postech.fase3parquimetro.domain.vehicle.repository;

import com.postech.fase3parquimetro.domain.vehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
}
