package com.bloodbridge.repository;

import com.bloodbridge.model.Donor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends MongoRepository<Donor, String> {
    Optional<Donor> findByEmail(String email);
    List<Donor> findByBloodType(String bloodType);
    List<Donor> findByIsActiveTrue();
}
