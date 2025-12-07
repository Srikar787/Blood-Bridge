package com.bloodbridge.service;

import com.bloodbridge.model.Donor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class EligibilityService {
    
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 65;
    private static final int MIN_DAYS_SINCE_LAST_DONATION = 56;
    
    public void updateEligibility(Donor donor) {
        boolean isEligible = checkEligibility(donor);
        donor.setEligible(isEligible);
    }
    
    public boolean computeEligibility(Donor donor) {
        updateEligibility(donor);
        return donor.isEligible();
    }
    
    public boolean checkEligibility(Donor donor) {
        // Check age
        if (donor.getAge() == null || donor.getAge() < MIN_AGE || donor.getAge() > MAX_AGE) {
            return false;
        }
        
        // Check last donation date
        if (donor.getLastDonationDate() != null) {
            long daysSinceLastDonation = ChronoUnit.DAYS.between(
                donor.getLastDonationDate(), LocalDate.now()
            );
            if (daysSinceLastDonation < MIN_DAYS_SINCE_LAST_DONATION) {
                return false;
            }
        }
        
        // Check required fields
        if (donor.getName() == null || donor.getName().isEmpty() ||
            donor.getEmail() == null || donor.getEmail().isEmpty() ||
            donor.getPhone() == null || donor.getPhone().isEmpty() ||
            donor.getBloodType() == null || donor.getBloodType().isEmpty() ||
            donor.getGender() == null || donor.getGender().isEmpty()) {
            return false;
        }
        
        // Check location
        if (donor.getLatitude() == null || donor.getLongitude() == null) {
            return false;
        }
        
        return true;
    }
    
    public String getEligibilityMessage(Donor donor) {
        if (checkEligibility(donor)) {
            return "Eligible to donate blood";
        }
        
        if (donor.getAge() == null || donor.getAge() < MIN_AGE || donor.getAge() > MAX_AGE) {
            return String.format("Age must be between %d and %d years", MIN_AGE, MAX_AGE);
        }
        
        if (donor.getLastDonationDate() != null) {
            long daysSinceLastDonation = ChronoUnit.DAYS.between(
                donor.getLastDonationDate(), LocalDate.now()
            );
            if (daysSinceLastDonation < MIN_DAYS_SINCE_LAST_DONATION) {
                return String.format("Must wait %d days since last donation. %d days remaining.", 
                    MIN_DAYS_SINCE_LAST_DONATION,
                    MIN_DAYS_SINCE_LAST_DONATION - daysSinceLastDonation);
    }
}

        return "Please complete all required fields including location";
    }
}
