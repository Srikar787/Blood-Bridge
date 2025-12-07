package com.bloodbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String email;
    private String password; // Hashed password
    private String name;
    private String role; // "DONOR" or "REQUESTOR"
    private String provider; // "local" or "google"
    private String providerId; // Google user ID if OAuth
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean isActive;
}
