package com.bloodbridge.dto;

import lombok.Data;

@Data
public class DonorSearchRequest {
    private String bloodType;
    private Double latitude;
    private Double longitude;
    private Double radiusKm; // Default 10km
    private String gender; // Optional filter
    private Integer minAge; // Optional filter
    private Integer maxAge; // Optional filter
}
