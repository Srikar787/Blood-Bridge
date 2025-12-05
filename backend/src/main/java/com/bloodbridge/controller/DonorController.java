package com.bloodbridge.controller;

import com.bloodbridge.model.Donor;
import com.bloodbridge.repository.DonorRepository;
import com.bloodbridge.service.EligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "http://localhost:3000")
public class DonorController {

    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private EligibilityService eligibilityService;

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        List<Donor> donors = donorRepository.findAll();
        // Update eligibility for all donors
        donors.forEach(eligibilityService::updateEligibility);
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable String id) {
        Optional<Donor> donor = donorRepository.findById(id);
        if (donor.isPresent()) {
            eligibilityService.updateEligibility(donor.get());
            return ResponseEntity.ok(donor.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<Donor>> getDonorsByBloodType(@PathVariable String bloodType) {
        List<Donor> donors = donorRepository.findByBloodType(bloodType);
        donors.forEach(eligibilityService::updateEligibility);
        return ResponseEntity.ok(donors);
    }

    @PostMapping
    public ResponseEntity<Donor> createDonor(@RequestBody Donor donor) {
        // Update eligibility before saving
        eligibilityService.updateEligibility(donor);
        Donor savedDonor = donorRepository.save(donor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDonor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable String id, @RequestBody Donor donor) {
        if (!donorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        donor.setId(id);
        // Update eligibility before saving
        eligibilityService.updateEligibility(donor);
        Donor updatedDonor = donorRepository.save(donor);
        return ResponseEntity.ok(updatedDonor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable String id) {
        if (!donorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        donorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("BloodBridge API is running!");
    }
}
