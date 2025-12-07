package com.bloodbridge.repository;

import com.bloodbridge.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByDonorId(String donorId);
    List<Notification> findByRequestorId(String requestorId);
    List<Notification> findByDonorEmail(String donorEmail);
    List<Notification> findByStatus(String status);
    Optional<Notification> findByIdAndDonorId(String id, String donorId);
}




