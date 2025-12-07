package com.bloodbridge.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String donorId;
    private String message;
    private String urgency; // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    private String bloodType;
    private String requestorName;
    private String requestorPhone;
}




