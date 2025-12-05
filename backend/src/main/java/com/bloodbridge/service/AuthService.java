package com.bloodbridge.service;

import com.bloodbridge.model.User;
import com.bloodbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    public User registerUser(String email, String password, String name, String role, String phone) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(role.toUpperCase());
        user.setProvider("local");
        user.setPhone(phone);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        
        return userRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        User user = userOpt.get();
        
        // Auto-activate inactive users (for development - remove in production)
        if (!user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
    }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return jwtService.generateToken(user.getEmail(), user.getRole());
    }

    public User findOrCreateGoogleUser(String email, String name, String providerId) {
        Optional<User> existingUser = userRepository.findByEmailAndProvider(email, "google");
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.save(user);
        }
        
        // Check if email exists with local provider
        Optional<User> localUser = userRepository.findByEmail(email);
        if (localUser.isPresent()) {
            throw new RuntimeException("Email already registered with local account. Please use email/password login.");
        }
        
        // Create new Google user
            User user = new User();
            user.setEmail(email);
            user.setName(name);
        user.setProvider("google");
        user.setProviderId(providerId);
        user.setRole("DONOR"); // Default role, can be changed later
            user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        
            return userRepository.save(user);
        }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public JwtService getJwtService() {
        return jwtService;
    }
}
