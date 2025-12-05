package com.bloodbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    @Id
    private String id;
    private String userId; // Reference to User
    private String name;
    private String email;
    private String phone;
    private String bloodType;
    private LocalDate lastDonationDate;
    private String address;
    private Double latitude; // For proximity search
    private Double longitude; // For proximity search
    private String gender;
    private Integer age;
    private boolean isActive;
    private boolean isEligible; // Based on eligibility criteria
}
