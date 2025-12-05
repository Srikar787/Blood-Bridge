package com.bloodbridge.service;

import com.bloodbridge.dto.DonorSearchRequest;
import com.bloodbridge.model.Donor;
import com.bloodbridge.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
    private final EligibilityService eligibilityService;
    private final LocationService locationService;

    // Haversine formula to compute distance in kilometers
    public double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        return locationService.calculateDistance(lat1, lon1, lat2, lon2);
    }

    /**
     * Find nearby eligible donors within specified radius
     */
    public List<Donor> findNearbyDonors(String bloodType, Double latitude, Double longitude, double radiusKm) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude and longitude are required");
        }

        // Default radius safeguard
        double searchRadius = radiusKm > 0 ? radiusKm : 10.0;

        // Normalize blood type for comparison
        String normalizedBloodType = bloodType != null ? bloodType.trim().toUpperCase() : "";

        // Fetch all donors and filter in-memory (case-insensitive blood type)
        List<Donor> allDonors = donorRepository.findAll();

        return allDonors.stream()
            .filter(donor -> {
                // Blood type check (case-insensitive)
                if (normalizedBloodType.length() > 0) {
                    if (donor.getBloodType() == null ||
                        !normalizedBloodType.equalsIgnoreCase(donor.getBloodType().trim())) {
                        return false;
                    }
                }

                // Check if donor has location
                if (donor.getLatitude() == null || donor.getLongitude() == null) {
                    return false;
                }
                
                // Update eligibility (but do not exclude; we return eligibility flag to the client)
                eligibilityService.updateEligibility(donor);
                
                // Check distance
                double distance = distanceKm(latitude, longitude, donor.getLatitude(), donor.getLongitude());
                return distance <= searchRadius;
            })
            .sorted(Comparator.comparing(donor -> 
                distanceKm(latitude, longitude, donor.getLatitude(), donor.getLongitude())
            ))
            .collect(Collectors.toList());
    }

    // Simple search implementation that fetches all donors and filters in-memory.
    // For production, replace with geo queries in MongoDB.
    public List<Donor> searchDonors(DonorSearchRequest req) {
        List<Donor> all = donorRepository.findAll();
        if (req == null) return all;

        Double lat = req.getLatitude();
        Double lon = req.getLongitude();
        Double radius = req.getRadiusKm() != null ? req.getRadiusKm() : Double.MAX_VALUE;

        List<Donor> out = new ArrayList<>();
        for (Donor d : all) {
            if (req.getBloodType() != null && !req.getBloodType().equalsIgnoreCase(d.getBloodType())) continue;
            if (req.getGender() != null && d.getGender() != null && !req.getGender().equalsIgnoreCase(d.getGender())) continue;
            if (req.getMinAge() != null && (d.getAge() == null || d.getAge() < req.getMinAge())) continue;
            if (req.getMaxAge() != null && (d.getAge() == null || d.getAge() > req.getMaxAge())) continue;

            if (lat != null && lon != null && d.getLatitude() != null && d.getLongitude() != null) {
                double km = distanceKm(lat, lon, d.getLatitude(), d.getLongitude());
                if (km > radius) continue;
            }
            // compute and set eligibility
            eligibilityService.updateEligibility(d);

            out.add(d);
        }
        return out;
    }
}
