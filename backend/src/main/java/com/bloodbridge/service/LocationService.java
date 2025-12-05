package com.bloodbridge.service;

import org.springframework.stereotype.Service;

@Service
public class LocationService {
    
    // Earth's radius in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;
    
    /**
     * Calculate distance between two coordinates using Haversine formula
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return Distance in kilometers
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
    
    /**
     * Calculate bounding box for proximity search
     * @param centerLat Center latitude
     * @param centerLon Center longitude
     * @param radiusKm Radius in kilometers
     * @return Array [minLat, maxLat, minLon, maxLon]
     */
    public double[] getBoundingBox(double centerLat, double centerLon, double radiusKm) {
        // Approximate degrees per kilometer (varies by latitude)
        double latDegreesPerKm = 1.0 / 111.0; // Roughly constant
        double lonDegreesPerKm = 1.0 / (111.0 * Math.cos(Math.toRadians(centerLat)));
        
        double latOffset = radiusKm * latDegreesPerKm;
        double lonOffset = radiusKm * lonDegreesPerKm;
        
        return new double[]{
            centerLat - latOffset, // minLat
            centerLat + latOffset, // maxLat
            centerLon - lonOffset, // minLon
            centerLon + lonOffset  // maxLon
        };
    }
}
