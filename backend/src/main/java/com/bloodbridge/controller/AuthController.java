package com.bloodbridge.controller;

import com.bloodbridge.dto.AuthResponse;
import com.bloodbridge.dto.LoginRequest;
import com.bloodbridge.dto.RegisterRequest;
import com.bloodbridge.model.User;
import com.bloodbridge.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.getRole().equalsIgnoreCase("DONOR") && 
                !request.getRole().equalsIgnoreCase("REQUESTOR")) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, null, null, "Role must be DONOR or REQUESTOR"));
            }

            User user = authService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getRole(),
                request.getPhone()
            );

            String token = authService.getJwtService().generateToken(user.getEmail(), user.getRole());

            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Registration successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateUser(request.getEmail(), request.getPassword());
            User user = authService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        try {
            if (oauth2User == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, null, "OAuth2 authentication failed"));
            }
            
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String providerId = oauth2User.getName();

            if (email == null || name == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, null, "Unable to retrieve user information from Google"));
            }

            User user = authService.findOrCreateGoogleUser(email, name, providerId);
            String token = authService.getJwtService().generateToken(user.getEmail(), user.getRole());

            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole(),
                "Google login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(null, null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<AuthResponse> oauth2Callback(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2Success(oauth2User);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        // Extract token and get user info
        // This would require token validation
        return ResponseEntity.ok().build();
    }
}
