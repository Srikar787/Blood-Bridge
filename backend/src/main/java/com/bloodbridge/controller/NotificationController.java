package com.bloodbridge.controller;

import com.bloodbridge.dto.NotificationRequest;
import com.bloodbridge.model.Notification;
import com.bloodbridge.service.EmailService;
import com.bloodbridge.service.NotificationService;
import com.bloodbridge.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final SmsService smsService;
    private final EmailService emailService;
    
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendNotification(
            @RequestBody NotificationRequest request,
            Authentication authentication) {
        try {
            // Get email from authentication principal (should be email string)
            String requestorEmail = authentication.getName();
            
            // Fallback: if principal is User object, extract email from it
            if (requestorEmail == null || requestorEmail.isEmpty() || !requestorEmail.contains("@")) {
                if (authentication.getPrincipal() instanceof com.bloodbridge.model.User) {
                    com.bloodbridge.model.User user = (com.bloodbridge.model.User) authentication.getPrincipal();
                    requestorEmail = user.getEmail();
                } else if (authentication.getDetails() instanceof com.bloodbridge.model.User) {
                    com.bloodbridge.model.User user = (com.bloodbridge.model.User) authentication.getDetails();
                    requestorEmail = user.getEmail();
                }
            }
            
            if (requestorEmail == null || requestorEmail.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Unable to determine requestor email from authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            Notification notification = notificationService.sendNotification(requestorEmail, request);
            
            // Check email and SMS configuration status
            Map<String, Object> emailStatus = emailService.getEmailConfigurationStatus();
            Map<String, Object> smsStatus = smsService.getSmsConfigurationStatus();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            // Determine message based on notification status
            if ("SENT".equals(notification.getStatus())) {
                // Check if email was sent (primary free method)
                if (notification.getDonorEmail() != null && !notification.getDonorEmail().isEmpty()) {
                    if ((Boolean) emailStatus.get("fullyConfigured")) {
                        response.put("message", "Notification sent successfully via email to donor");
                        
                        // Add SMS status info
                        if (notification.getDonorPhone() != null && !notification.getDonorPhone().isEmpty()) {
                            // Check if Indian number
                            String phoneDigits = notification.getDonorPhone().replaceAll("[^\\d]", "");
                            boolean isIndianNumber = (phoneDigits.startsWith("91") && phoneDigits.length() == 12) || 
                                                   (phoneDigits.length() == 10 && phoneDigits.matches("^[6-9]\\d{9}$"));
                            
                            if (isIndianNumber) {
                                response.put("smsInfo", "SMS attempted via email-to-SMS gateway, but delivery is NOT guaranteed for Indian carriers. Email notification is reliable.");
                                response.put("smsWarning", "Indian carriers often don't support email-to-SMS. Email notification was sent successfully.");
                            } else if ((Boolean) smsStatus.get("fullyConfigured")) {
                                response.put("smsInfo", "SMS also sent via email-to-SMS gateway");
                            } else {
                                response.put("smsInfo", "SMS not configured. Email notification sent successfully.");
                            }
                        }
                    } else {
                        response.put("message", "Notification saved. Email not sent - email configuration incomplete.");
                        response.put("emailWarning", emailStatus.get("missingConfig"));
                    }
                } else {
                    response.put("message", "Notification saved. Donor email address not available.");
                }
            } else {
                response.put("message", "Notification saved but not sent. Please check email configuration.");
                response.put("emailWarning", emailStatus.get("missingConfig"));
            }
            
            response.put("notification", notification);
            response.put("emailConfig", emailStatus);
            response.put("smsConfig", smsStatus);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Notification>> getNotificationsByDonor(@PathVariable String donorId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByDonorId(donorId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/my-notifications")
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
        try {
            // Get email from authentication principal (should be email string)
            String userEmail = authentication.getName();
            
            // Fallback: if principal is User object, extract email from it
            if (userEmail == null || userEmail.isEmpty() || !userEmail.contains("@")) {
                if (authentication.getPrincipal() instanceof com.bloodbridge.model.User) {
                    com.bloodbridge.model.User user = (com.bloodbridge.model.User) authentication.getPrincipal();
                    userEmail = user.getEmail();
                } else if (authentication.getDetails() instanceof com.bloodbridge.model.User) {
                    com.bloodbridge.model.User user = (com.bloodbridge.model.User) authentication.getDetails();
                    userEmail = user.getEmail();
                }
            }
            
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            List<Notification> notifications = notificationService.getNotificationsByRequestorEmail(userEmail);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/sms-status")
    public ResponseEntity<Map<String, Object>> getSmsStatus() {
        Map<String, Object> status = smsService.getSmsConfigurationStatus();
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/email-status")
    public ResponseEntity<Map<String, Object>> getEmailStatus() {
        Map<String, Object> status = emailService.getEmailConfigurationStatus();
        return ResponseEntity.ok(status);
    }
}

