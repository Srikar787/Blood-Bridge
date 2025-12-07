package com.bloodbridge.config;

import com.bloodbridge.service.JwtService;
import com.bloodbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(7);
        
        try {
            String email = jwtService.extractEmail(token);
            if (email == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    var user = userOpt.get();
                    
                    // Extract role from token or user
                    String role = jwtService.extractRole(token);
                    
                    // Fallback to user's role if not in token
                    if (role == null || role.isEmpty()) {
                        role = user.getRole();
                    }
                    
                    // Create authorities with ROLE_ prefix (Spring Security convention)
                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                    if (role != null && !role.isEmpty()) {
                        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
                        authorities = Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
                    }
                    
                    // Use email as principal so authentication.getName() returns email
                    var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    auth.setDetails(user); // Store User object in details for easy access
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("Authenticated user: {} with role: {}", email, role);
                }
            }
        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            // Continue filter chain - let Spring Security handle unauthorized access
        }

        filterChain.doFilter(request, response);
    }
}
