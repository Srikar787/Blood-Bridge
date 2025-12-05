package com.bloodbridge.controller;

import com.bloodbridge.model.User;
import com.bloodbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Activate a user account
     * For development/testing purposes
     */
    @PostMapping("/users/{email}/activate")
    public ResponseEntity<Map<String, Object>> activateUser(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.notFound().build();
        }
        
        User user = userOpt.get();
        user.setActive(true);
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User activated successfully");
        response.put("email", user.getEmail());
        response.put("active", user.isActive());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users (for admin purposes)
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by email
     */
    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
}

