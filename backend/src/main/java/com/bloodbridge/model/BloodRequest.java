package com.bloodbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "blood_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequest {
    @Id
    private String id;
    private String requestorId; // Reference to User
    private String patientName;
    private String bloodType;
    private Integer unitsRequired;
    private String hospitalName;
    private String hospitalAddress;
    private Double latitude;
    private Double longitude;
    private LocalDate requiredDate;
    private String urgency; // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    private String status; // "PENDING", "FULFILLED", "CANCELLED"
    private String contactPhone;
    private String additionalNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
