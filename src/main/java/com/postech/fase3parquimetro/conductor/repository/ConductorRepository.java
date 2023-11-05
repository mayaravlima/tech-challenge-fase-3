package com.postech.fase3parquimetro.conductor.repository;

import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConductorRepository extends MongoRepository<ConductorEntity, String> {

    @Query("{'vehicle.$id': ?0}")
    ConductorEntity findByVehicleId(String vehicleId);

    @Cacheable(value = "conductor", key="#id", condition="#id!=null")
    Optional<ConductorEntity> findByEmail(String email);

    @Cacheable(value = "conductor", key="#id", condition="#id!=null")
    Optional<ConductorEntity> findByPhone(String phone);
}