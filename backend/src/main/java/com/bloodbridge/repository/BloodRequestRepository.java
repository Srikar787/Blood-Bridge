package com.bloodbridge.repository;

import com.bloodbridge.model.BloodRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends MongoRepository<BloodRequest, String> {
    List<BloodRequest> findByRequestorId(String requestorId);
    List<BloodRequest> findByStatus(String status);
    List<BloodRequest> findByBloodTypeAndStatus(String bloodType, String status);
}

