package com.bloodbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private String id;
    private String donorId; // Donor being contacted
    private String donorEmail; // Donor's email
    private String donorName; // Donor's name
    private String donorPhone; // Donor's phone number
    private String requestorId; // User sending the notification
    private String requestorEmail; // Requestor's email
    private String requestorName; // Requestor's name
    private String requestorPhone; // Requestor's phone
    private String message; // Custom message from requestor
    private String bloodType; // Required blood type
    private String urgency; // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    private String status; // "PENDING", "SENT", "READ", "RESPONDED"
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private String n8nWebhookUrl; // Optional: n8n webhook URL for integration
}


