package com.bloodbridge.controller;

import com.bloodbridge.dto.DonorSearchRequest;
import com.bloodbridge.model.Donor;
import com.bloodbridge.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donors/search")
@CrossOrigin(origins = "http://localhost:3000")
public class DonorSearchController {

    @Autowired
    private DonorService donorService;

    @PostMapping("/nearby")
    public ResponseEntity<Map<String, Object>> searchNearbyDonors(@RequestBody DonorSearchRequest request) {
        try {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Latitude and longitude are required");
                return ResponseEntity.badRequest().body(error);
            }

            if (request.getBloodType() == null || request.getBloodType().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Blood type is required");
                return ResponseEntity.badRequest().body(error);
            }

            double radiusKm = (request.getRadiusKm() != null && request.getRadiusKm() > 0) ? request.getRadiusKm() : 10.0;

            List<Donor> donors = donorService.findNearbyDonors(
                request.getBloodType(),
                request.getLatitude(),
                request.getLongitude(),
                radiusKm
            );

            // Apply additional filters if provided
            if (request.getGender() != null && !request.getGender().isEmpty()) {
                donors = donors.stream()
                    .filter(d -> request.getGender().equalsIgnoreCase(d.getGender()))
                    .toList();
            }

            if (request.getMinAge() != null) {
                donors = donors.stream()
                    .filter(d -> d.getAge() != null && d.getAge() >= request.getMinAge())
                    .toList();
            }

            if (request.getMaxAge() != null) {
                donors = donors.stream()
                    .filter(d -> d.getAge() != null && d.getAge() <= request.getMaxAge())
                    .toList();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("donors", donors);
            response.put("count", donors.size());
            response.put("radiusKm", radiusKm);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/eligibility-criteria")
    public ResponseEntity<Map<String, Object>> getEligibilityCriteria() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("minAge", 18);
        criteria.put("maxAge", 65);
        criteria.put("minDaysSinceLastDonation", 56);
        criteria.put("requiredFields", List.of("name", "email", "phone", "bloodType", "age", "gender", "location"));
        
        return ResponseEntity.ok(criteria);
    }
}
