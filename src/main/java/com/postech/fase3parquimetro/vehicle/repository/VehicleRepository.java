package com.postech.fase3parquimetro.vehicle.repository;

import com.postech.fase3parquimetro.vehicle.model.VehicleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<VehicleEntity, String> {

    public Optional<VehicleEntity> findByPlate(String plate);

    @Query(value = "{ 'parking.$id': ?0 }")
    VehicleEntity findByParkingId(String parkingId);
}