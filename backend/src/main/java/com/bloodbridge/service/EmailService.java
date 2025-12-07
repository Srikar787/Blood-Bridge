package com.bloodbridge.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    @Value("${email.enabled:true}")
    private boolean emailEnabled;
    
    @Value("${email.from.name:BloodBridge}")
    private String fromName;
    
    /**
     * Send email notification to donor
     * @param toEmail Recipient email address
     * @param donorName Donor's name
     * @param requestorName Requestor's name
     * @param requestorPhone Requestor's phone number
     * @param requestorEmail Requestor's email
     * @param message Custom message
     * @param bloodType Required blood type
     * @param urgency Urgency level
     * @return true if sent successfully, false otherwise
     */
    public boolean sendNotificationEmail(String toEmail, String donorName, String requestorName,
                                        String requestorPhone, String requestorEmail, String message,
                                        String bloodType, String urgency) {
        if (!emailEnabled) {
            log.warn("Email notifications are disabled. Enable with email.enabled=true");
            return false;
        }
        
        if (toEmail == null || toEmail.isEmpty()) {
            log.error("Cannot send email: recipient email is empty");
            return false;
        }
        
        if (fromEmail == null || fromEmail.isEmpty()) {
            log.error("Cannot send email: sender email (spring.mail.username) not configured");
            return false;
        }
        
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(fromEmail);
            email.setTo(toEmail);
            email.setSubject("BloodBridge Alert: Blood Donation Request - " + urgency);
            
            // Build email body
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Dear ").append(donorName).append(",\n\n");
            emailBody.append("You have received a blood donation request through BloodBridge.\n\n");
            emailBody.append("═══════════════════════════════════════\n");
            emailBody.append("REQUEST DETAILS\n");
            emailBody.append("═══════════════════════════════════════\n\n");
            emailBody.append("Blood Type Required: ").append(bloodType).append("\n");
            emailBody.append("Urgency Level: ").append(urgency).append("\n\n");
            emailBody.append("Requestor Information:\n");
            emailBody.append("  Name: ").append(requestorName).append("\n");
            if (requestorPhone != null && !requestorPhone.isEmpty()) {
                emailBody.append("  Phone: ").append(requestorPhone).append("\n");
            }
            if (requestorEmail != null && !requestorEmail.isEmpty()) {
                emailBody.append("  Email: ").append(requestorEmail).append("\n");
            }
            emailBody.append("\n");
            
            if (message != null && !message.isEmpty()) {
                emailBody.append("Message from Requestor:\n");
                emailBody.append("─────────────────────────────────────\n");
                emailBody.append(message).append("\n");
                emailBody.append("─────────────────────────────────────\n\n");
            }
            
            emailBody.append("═══════════════════════════════════════\n");
            emailBody.append("Please contact the requestor if you can help.\n");
            emailBody.append("Your donation can save a life!\n\n");
            emailBody.append("Thank you for being a blood donor.\n\n");
            emailBody.append("Best regards,\n");
            emailBody.append(fromName).append(" Team\n");
            emailBody.append("BloodBridge - Connecting Donors with Those in Need");
            
            email.setText(emailBody.toString());
            
            mailSender.send(email);
            log.info("Email notification sent successfully to: {}", toEmail);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email notification to {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Check if email is properly configured
     * @return Map with configuration status
     */
    public java.util.Map<String, Object> getEmailConfigurationStatus() {
        java.util.Map<String, Object> status = new java.util.HashMap<>();
        status.put("enabled", emailEnabled);
        status.put("hasFromEmail", fromEmail != null && !fromEmail.isEmpty());
        status.put("hasMailSender", mailSender != null);
        
        boolean fullyConfigured = emailEnabled && 
            (Boolean) status.get("hasFromEmail") && 
            (Boolean) status.get("hasMailSender");
        status.put("fullyConfigured", fullyConfigured);
        
        if (!fullyConfigured) {
            java.util.List<String> missing = new java.util.ArrayList<>();
            if (!emailEnabled) missing.add("Email is disabled (set email.enabled=true)");
            if (!(Boolean) status.get("hasFromEmail")) missing.add("Sender email not configured (set spring.mail.username)");
            if (!(Boolean) status.get("hasMailSender")) missing.add("Mail sender not available");
            status.put("missingConfig", missing);
        }
        
        return status;
    }
}

