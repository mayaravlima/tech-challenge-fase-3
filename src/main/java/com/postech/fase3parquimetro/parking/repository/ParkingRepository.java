package com.postech.fase3parquimetro.parking.repository;

import com.postech.fase3parquimetro.parking.model.ParkingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepository extends MongoRepository<ParkingEntity, String> {

    @Query("{ 'status': { $in: [ 'EXPIRED', 'NEAR_EXPIRATION' ] } }")
    Optional<List<ParkingEntity>> findExpiredOrNearExpirationStatus();

}
