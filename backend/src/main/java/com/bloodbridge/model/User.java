package com.bloodbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    private String email;
    private String password;
    private String name;
    private String role;          // "DONOR" or "REQUESTOR"
    private String provider;      // "local" or "google"
    private String providerId;    // Google OAuth ID
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean isActive;

    // ----------------------------
    // UserDetails implementation
    // ----------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList();
        }
        // Converts "DONOR" → "ROLE_DONOR"
        return List.of(new SimpleGrantedAuthority(role.toUpperCase()));
    }

    @Override
    public String getUsername() {
        return email; // Login identifier
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
