package com.bloodbridge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    @Lazy
    private com.bloodbridge.service.AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/donors/health", "/oauth2/**", "/login/oauth2/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Allow authenticated users (any role) to view donors list
                .requestMatchers(HttpMethod.GET, "/api/donors").hasAnyRole("DONOR", "REQUESTOR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/donors/{id}").hasAnyRole("DONOR", "REQUESTOR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/donors/blood-type/**").hasAnyRole("DONOR", "REQUESTOR", "ADMIN")
                // Only ADMIN can delete or update donors
                .requestMatchers(HttpMethod.DELETE, "/api/donors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/donors/**").hasRole("ADMIN")
                // Donor registration requires authenticated users (any role)
                .requestMatchers(HttpMethod.POST, "/api/donors").hasAnyRole("DONOR", "REQUESTOR", "ADMIN")
                .requestMatchers("/api/requestors/**").hasAnyRole("REQUESTOR", "DONOR", "ADMIN")
                // Notification endpoints - authenticated users can send notifications
                .requestMatchers("/api/notifications/**").hasAnyRole("REQUESTOR", "DONOR", "ADMIN")
                // Allow search endpoints to be public so requestors can search without friction
                .requestMatchers("/api/donors/search/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    // Get OAuth2User
                    if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                        org.springframework.security.oauth2.core.user.OAuth2User oauth2User = 
                            (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                        
                        String email = oauth2User.getAttribute("email");
                        String name = oauth2User.getAttribute("name");
                        String providerId = oauth2User.getName();
                        
                        // Create or find user and generate token
                        try {
                            com.bloodbridge.model.User user = authService.findOrCreateGoogleUser(email, name, providerId);
                            String token = authService.getJwtService().generateToken(user.getEmail(), user.getRole());
                            
                            // Redirect to frontend with token
                            response.sendRedirect("http://localhost:3000/auth/callback?token=" + token + 
                                "&email=" + user.getEmail() + "&name=" + user.getName() + "&role=" + user.getRole());
                        } catch (Exception e) {
                            response.sendRedirect("http://localhost:3000/login?error=" + 
                                java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
                        }
                    } else {
                        response.sendRedirect("http://localhost:3000/login?error=oauth_failed");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("http://localhost:3000/login?error=oauth_failed");
                })
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
