package com.postech.fase3parquimetro.domain.conductor.repository;

import com.postech.fase3parquimetro.domain.conductor.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductorRepository extends MongoRepository<Conductor, String> {
}
