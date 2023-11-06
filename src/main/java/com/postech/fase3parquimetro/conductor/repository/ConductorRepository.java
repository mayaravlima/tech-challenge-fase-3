package com.postech.fase3parquimetro.conductor.repository;

import com.postech.fase3parquimetro.conductor.model.ConductorEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConductorRepository extends MongoRepository<ConductorEntity, String> {


    ConductorEntity findByVehiclesId(String vehicleId);

    ConductorEntity findByPaymentsId(String paymentId);

    @Cacheable(value = "conductor", key = "#id", condition = "#id!=null")
    Optional<ConductorEntity> findByEmail(String email);

    @Cacheable(value = "conductor", key = "#id", condition = "#id!=null")
    Optional<ConductorEntity> findByPhone(String phone);
}