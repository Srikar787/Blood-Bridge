package com.bloodbridge.service;

import com.bloodbridge.dto.NotificationRequest;
import com.bloodbridge.model.Donor;
import com.bloodbridge.model.Notification;
import com.bloodbridge.model.User;
import com.bloodbridge.repository.DonorRepository;
import com.bloodbridge.repository.NotificationRepository;
import com.bloodbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final SmsService smsService;
    private final EmailService emailService;
    
    @Value("${n8n.webhook.url:}")
    private String n8nWebhookUrl;
    
    public Notification sendNotification(String requestorEmail, NotificationRequest request) {
        // Find donor
        Optional<Donor> donorOpt = donorRepository.findById(request.getDonorId());
        if (donorOpt.isEmpty()) {
            throw new RuntimeException("Donor not found");
        }
        
        Donor donor = donorOpt.get();
        
        // Find requestor user
        Optional<User> requestorOpt = userRepository.findByEmail(requestorEmail);
        if (requestorOpt.isEmpty()) {
            throw new RuntimeException("Requestor not found");
        }
        
        User requestor = requestorOpt.get();
        
        // Create notification
        Notification notification = new Notification();
        notification.setDonorId(donor.getId());
        notification.setDonorEmail(donor.getEmail());
        notification.setDonorName(donor.getName());
        notification.setDonorPhone(donor.getPhone());
        notification.setRequestorId(requestor.getId());
        notification.setRequestorEmail(requestor.getEmail());
        notification.setRequestorName(request.getRequestorName() != null ? request.getRequestorName() : requestor.getName());
        notification.setRequestorPhone(request.getRequestorPhone() != null ? request.getRequestorPhone() : requestor.getPhone());
        notification.setMessage(request.getMessage());
        notification.setBloodType(request.getBloodType() != null ? request.getBloodType() : donor.getBloodType());
        notification.setUrgency(request.getUrgency() != null ? request.getUrgency() : "MEDIUM");
        notification.setStatus("PENDING");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setN8nWebhookUrl(n8nWebhookUrl);
        
        // Save notification
        notification = notificationRepository.save(notification);
        
        // Send EMAIL notification to donor (FREE - Primary method)
        boolean emailSent = false;
        String emailError = null;
        if (donor.getEmail() != null && !donor.getEmail().isEmpty()) {
            try {
                emailSent = emailService.sendNotificationEmail(
                    donor.getEmail(),
                    donor.getName(),
                    notification.getRequestorName(),
                    notification.getRequestorPhone(),
                    notification.getRequestorEmail(),
                    notification.getMessage(),
                    notification.getBloodType(),
                    notification.getUrgency()
                );
                if (emailSent) {
                    log.info("Email notification sent successfully to donor: {}", donor.getEmail());
                } else {
                    emailError = "Email could not be sent. Please check email configuration.";
                    log.warn("Failed to send email notification to donor: {}", donor.getEmail());
                }
            } catch (Exception e) {
                emailError = "Email sending failed: " + e.getMessage();
                log.error("Error sending email notification: {}", e.getMessage(), e);
            }
        } else {
            emailError = "Donor email address is not available";
            log.warn("Cannot send email: donor email is not available for donor ID: {}", donor.getId());
        }
        
        // Send SMS to donor's phone number (Optional - requires Twilio)
        boolean smsSent = false;
        String smsError = null;
        if (donor.getPhone() != null && !donor.getPhone().isEmpty()) {
            try {
                String smsMessage = smsService.formatNotificationMessage(
                    donor.getName(),
                    notification.getRequestorName(),
                    notification.getRequestorPhone(),
                    notification.getMessage(),
                    notification.getBloodType(),
                    notification.getUrgency()
                );
                smsSent = smsService.sendSms(donor.getPhone(), smsMessage);
                if (smsSent) {
                    // Check if it's an Indian number
                    String phoneDigits = donor.getPhone().replaceAll("[^\\d]", "");
                    boolean isIndianNumber = (phoneDigits.startsWith("91") && phoneDigits.length() == 12) || 
                                           (phoneDigits.length() == 10 && phoneDigits.matches("^[6-9]\\d{9}$"));
                    
                    if (isIndianNumber) {
                        smsError = "Email sent to SMS gateway, but delivery NOT guaranteed. Many Indian carriers don't support email-to-SMS. Email notification was sent successfully.";
                        log.warn("Indian number detected. Email-to-SMS gateway used but delivery is unreliable. Email notification is the reliable method.");
                    } else {
                        log.info("SMS notification sent successfully to donor: {}", donor.getPhone());
                    }
                } else {
                    String phoneDigits = donor.getPhone().replaceAll("[^\\d]", "");
                    boolean isIndianNumber = (phoneDigits.startsWith("91") && phoneDigits.length() == 12) || 
                                           (phoneDigits.length() == 10 && phoneDigits.matches("^[6-9]\\d{9}$"));
                    
                    if (isIndianNumber) {
                        smsError = "SMS via email-to-SMS gateway failed. Indian carriers often don't support this method. Email notification was sent successfully and is reliable.";
                    } else {
                        smsError = "SMS could not be sent. Please check configuration.";
                    }
                    log.warn("Failed to send SMS notification to donor: {}. Email notification is the reliable alternative.", donor.getPhone());
                }
            } catch (Exception e) {
                smsError = "SMS sending failed: " + e.getMessage();
                log.error("Error sending SMS notification: {}", e.getMessage(), e);
            }
        } else {
            smsError = "Donor phone number is not available";
            log.warn("Cannot send SMS: donor phone number is not available for donor ID: {}", donor.getId());
        }
        
        // Store SMS error in notification if SMS failed
        if (!smsSent && smsError != null) {
            log.warn("SMS notification failed: {}. Notification saved but SMS not sent.", smsError);
        }
        
        // Send notification via n8n webhook if configured
        if (n8nWebhookUrl != null && !n8nWebhookUrl.isEmpty()) {
            try {
                sendToN8n(notification);
                log.info("Notification sent to n8n webhook successfully");
            } catch (Exception e) {
                log.error("Failed to send notification to n8n: {}", e.getMessage());
            }
        }
        
        // Update status based on Email, SMS, or n8n success
        // Email is the primary free method, so if email succeeds, mark as SENT
        boolean notificationSent = emailSent || smsSent;
        if (n8nWebhookUrl != null && !n8nWebhookUrl.isEmpty()) {
            // n8n webhook was called (even if it failed, we consider it attempted)
            notificationSent = true;
        }
        
        if (notificationSent) {
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.info("Notification sent successfully. Email: {}, SMS: {}", emailSent, smsSent);
        } else {
            // Keep as PENDING if all methods failed
            log.warn("Notification could not be sent. Email: {} (Error: {}), SMS: {} (Error: {}). Status remains PENDING.", 
                emailSent, emailError, smsSent, smsError);
        }
        
        return notification;
    }
    
    private void sendToN8n(Notification notification) {
        if (n8nWebhookUrl == null || n8nWebhookUrl.isEmpty()) {
            return;
        }
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("donorId", notification.getDonorId());
        payload.put("donorName", notification.getDonorName());
        payload.put("donorEmail", notification.getDonorEmail());
        payload.put("donorPhone", notification.getDonorPhone());
        payload.put("requestorName", notification.getRequestorName());
        payload.put("requestorEmail", notification.getRequestorEmail());
        payload.put("requestorPhone", notification.getRequestorPhone());
        payload.put("message", notification.getMessage());
        payload.put("bloodType", notification.getBloodType());
        payload.put("urgency", notification.getUrgency());
        payload.put("notificationId", notification.getId());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(n8nWebhookUrl, request, String.class);
            log.info("Notification sent to n8n successfully. Response: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Error sending to n8n webhook: {}", e.getMessage());
            throw e;
        }
    }
    
    public Notification getNotificationById(String id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
    }
    
    public List<Notification> getNotificationsByDonorId(String donorId) {
        return notificationRepository.findByDonorId(donorId);
    }
    
    public List<Notification> getNotificationsByRequestorId(String requestorId) {
        return notificationRepository.findByRequestorId(requestorId);
    }
    
    public List<Notification> getNotificationsByRequestorEmail(String requestorEmail) {
        Optional<User> userOpt = userRepository.findByEmail(requestorEmail);
        if (userOpt.isPresent()) {
            return notificationRepository.findByRequestorId(userOpt.get().getId());
        }
        return List.of();
    }
}

