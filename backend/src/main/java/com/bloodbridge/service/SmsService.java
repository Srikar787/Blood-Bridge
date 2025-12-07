package com.bloodbridge.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    
    private final JavaMailSender mailSender;
    
    @Value("${twilio.account.sid:}")
    private String accountSid;
    
    @Value("${twilio.auth.token:}")
    private String authToken;
    
    @Value("${twilio.phone.number:}")
    private String twilioPhoneNumber;
    
    @Value("${sms.enabled:false}")
    private boolean smsEnabled;
    
    @Value("${sms.use.free.gateway:true}")
    private boolean useFreeGateway;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    // US Carrier Email-to-SMS Gateways (FREE)
    private static final List<String> US_CARRIER_GATEWAYS = Arrays.asList(
        "@txt.att.net",           // AT&T
        "@vtext.com",             // Verizon
        "@tmomail.net",           // T-Mobile
        "@messaging.sprintpcs.com", // Sprint
        "@email.uscc.net",        // US Cellular
        "@sms.cricketwireless.net", // Cricket
        "@sms.myboostmobile.com", // Boost Mobile
        "@vmobl.com"              // Virgin Mobile
    );
    
    // Indian Carrier Email-to-SMS Gateways (FREE)
    private static final List<String> INDIA_CARRIER_GATEWAYS = Arrays.asList(
        "@airtelmail.com",        // Airtel
        "@airtelap.com",          // Airtel (alternative)
        "@vodaemail.co.in",       // Vodafone Idea
        "@vodaemail.com",         // Vodafone (alternative)
        "@ideacellular.net",      // Idea (now merged with Vodafone)
        "@bsnl.co.in",            // BSNL
        "@rjil.co.in",            // Reliance Jio
        "@tatadocomo.com"         // Tata Docomo
    );
    
    /**
     * Initialize Twilio client if credentials are provided
     */
    private void initializeTwilio() {
        if (accountSid != null && !accountSid.isEmpty() && 
            authToken != null && !authToken.isEmpty()) {
            try {
                Twilio.init(accountSid, authToken);
                log.info("Twilio initialized successfully");
            } catch (Exception e) {
                log.error("Failed to initialize Twilio: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Send SMS message to a phone number
     * Uses free email-to-SMS gateway by default, falls back to Twilio if configured
     * @param toPhoneNumber Recipient phone number (E.164 format: +1234567890)
     * @param messageText Message content
     * @return true if sent successfully, false otherwise
     */
    public boolean sendSms(String toPhoneNumber, String messageText) {
        if (!smsEnabled) {
            log.warn("SMS is disabled. Enable it in application.properties with sms.enabled=true");
            return false;
        }
        
        if (toPhoneNumber == null || toPhoneNumber.isEmpty()) {
            log.error("Cannot send SMS: recipient phone number is empty");
            return false;
        }
        
        if (messageText == null || messageText.isEmpty()) {
            log.error("Cannot send SMS: message text is empty");
            return false;
        }
        
        // Try free email-to-SMS gateway first (if enabled)
        if (useFreeGateway) {
            boolean freeSmsSent = sendSmsViaEmailGateway(toPhoneNumber, messageText);
            if (freeSmsSent) {
                return true;
            }
            log.info("Free email-to-SMS gateway failed, trying Twilio if configured...");
        }
        
        // Fall back to Twilio if free gateway failed or disabled
        if (accountSid != null && !accountSid.isEmpty() && 
            authToken != null && !authToken.isEmpty() &&
            twilioPhoneNumber != null && !twilioPhoneNumber.isEmpty() &&
            !accountSid.equals("YOUR_TWILIO_ACCOUNT_SID")) {
            try {
                initializeTwilio();
                
                // Normalize phone number (ensure it starts with +)
                String normalizedPhone = normalizePhoneNumber(toPhoneNumber);
                
                Message message = Message.creator(
                    new PhoneNumber(normalizedPhone),
                    new PhoneNumber(twilioPhoneNumber),
                    messageText
                ).create();
                
                log.info("SMS sent successfully via Twilio to {} with SID: {}", normalizedPhone, message.getSid());
                return true;
            } catch (Exception e) {
                log.error("Failed to send SMS via Twilio to {}: {}", toPhoneNumber, e.getMessage());
                return false;
            }
        }
        
        log.warn("SMS could not be sent: Free gateway failed and Twilio not configured");
        return false;
    }
    
    /**
     * Send SMS via free email-to-SMS gateway
     * This is completely FREE and uses existing email infrastructure
     * @param toPhoneNumber Recipient phone number
     * @param messageText Message content
     * @return true if sent successfully, false otherwise
     */
    private boolean sendSmsViaEmailGateway(String toPhoneNumber, String messageText) {
        if (mailSender == null) {
            log.warn("Cannot use free SMS gateway: Mail sender not available. Configure email first.");
            return false;
        }
        
        if (fromEmail == null || fromEmail.isEmpty()) {
            log.warn("Cannot use free SMS gateway: Sender email not configured");
            return false;
        }
        
        try {
            // Normalize phone number
            String normalizedPhone = normalizePhoneNumber(toPhoneNumber);
            
            // Detect country
            String country = detectCountry(normalizedPhone);
            
            // Extract digits only
            String digitsOnly = normalizedPhone.replaceAll("[^\\d]", "");
            String localNumber;
            List<String> gateways;
            
            if ("IN".equals(country)) {
                // India: Extract 10-digit number (remove +91 country code)
                if (digitsOnly.startsWith("91") && digitsOnly.length() == 12) {
                    localNumber = digitsOnly.substring(2); // Remove 91
                } else if (digitsOnly.length() == 10) {
                    localNumber = digitsOnly;
                } else {
                    log.warn("Cannot send SMS via gateway: Invalid Indian phone number format: {}", toPhoneNumber);
                    return false;
                }
                gateways = INDIA_CARRIER_GATEWAYS;
                log.info("Detected Indian number, using Indian carrier gateways");
            } else if ("US".equals(country)) {
                // US: Extract 10-digit number (remove +1 country code)
                if (digitsOnly.length() == 11 && digitsOnly.startsWith("1")) {
                    localNumber = digitsOnly.substring(1); // Remove 1
                } else if (digitsOnly.length() == 10) {
                    localNumber = digitsOnly;
                } else {
                    log.warn("Cannot send SMS via gateway: Invalid US phone number format: {}", toPhoneNumber);
                    return false;
                }
                gateways = US_CARRIER_GATEWAYS;
                log.info("Detected US number, using US carrier gateways");
            } else {
                log.warn("Cannot send SMS via gateway: Unsupported country for phone number: {}", toPhoneNumber);
                return false;
            }
            
            // Try sending to multiple carriers (one will work if the number is valid)
            // Most carriers will silently ignore if the number isn't theirs
            boolean sent = false;
            List<String> attemptedGateways = new ArrayList<>();
            
            for (String gateway : gateways) {
                String smsEmail = localNumber + gateway;
                
                try {
                    SimpleMailMessage email = new SimpleMailMessage();
                    email.setFrom(fromEmail);
                    email.setTo(smsEmail);
                    email.setSubject(""); // Empty subject for SMS
                    email.setText(messageText); // SMS message (keep it short)
                    
                    mailSender.send(email);
                    log.info("Email sent to SMS gateway: {} ({} carrier). Note: Carrier may not convert to SMS.", smsEmail, country);
                    // Note: Email sending succeeds, but carrier may not convert to SMS
                    // This is especially common with Indian carriers
                    sent = true;
                    log.warn("IMPORTANT: Email sent to gateway, but SMS delivery is NOT guaranteed. Indian carriers often don't support email-to-SMS.");
                    break; // Continue to next carrier if this fails
                } catch (Exception e) {
                    log.debug("Failed to send email to gateway {}: {}", gateway, e.getMessage());
                    attemptedGateways.add(gateway + " (error: " + e.getMessage() + ")");
                    // Continue to next carrier
                }
            }
            
            if (sent) {
                log.warn("Email sent to SMS gateway for {} ({}), but SMS delivery is NOT guaranteed. Many Indian carriers don't support email-to-SMS.", localNumber, country);
                // Return true because email was sent, but add warning
                return true;
            } else {
                log.error("Failed to send SMS via email-to-SMS gateway for {}. Attempted {} gateways: {}. " +
                    "Indian carriers often don't support email-to-SMS. Consider using email notifications only or a paid SMS service.", 
                    country, gateways.size(), attemptedGateways);
                return false;
            }
        } catch (Exception e) {
            log.error("Error sending SMS via email-to-SMS gateway: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Normalize phone number to E.164 format
     * Supports US (+1) and India (+91) numbers
     * @param phoneNumber Phone number in any format
     * @return Normalized phone number with + prefix
     */
    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }
        
        // Remove all non-digit characters except +
        String cleaned = phoneNumber.replaceAll("[^+\\d]", "");
        
        if (cleaned.startsWith("+")) {
            return cleaned; // Already in E.164 format
        }
        
        // Detect country based on number length and prefix
        if (cleaned.startsWith("91") && cleaned.length() == 12) {
            // India number with country code
            return "+" + cleaned;
        } else if (cleaned.startsWith("91") && cleaned.length() == 11) {
            // India number starting with 91 (but should be 10 digits)
            return "+" + cleaned;
        } else if (cleaned.length() == 10 && (cleaned.startsWith("6") || cleaned.startsWith("7") || 
                 cleaned.startsWith("8") || cleaned.startsWith("9"))) {
            // Likely Indian number (10 digits starting with 6-9)
            return "+91" + cleaned;
        } else if (cleaned.startsWith("1") && cleaned.length() == 11) {
            // US number with country code
            return "+" + cleaned;
        } else if (cleaned.length() == 10) {
            // Assume US number (10 digits)
            return "+1" + cleaned;
        } else {
            // Default: add + prefix
            return "+" + cleaned;
        }
    }
    
    /**
     * Detect country code from phone number
     * @param phoneNumber Normalized phone number
     * @return Country code (e.g., "US", "IN") or "UNKNOWN"
     */
    private String detectCountry(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "UNKNOWN";
        }
        
        String digitsOnly = phoneNumber.replaceAll("[^\\d]", "");
        
        if (phoneNumber.startsWith("+91") || (digitsOnly.startsWith("91") && digitsOnly.length() >= 10)) {
            return "IN"; // India
        } else if (phoneNumber.startsWith("+1") || (digitsOnly.startsWith("1") && digitsOnly.length() == 11)) {
            return "US"; // United States
        }
        
        return "UNKNOWN";
    }
    
    /**
     * Format notification message for SMS
     * @param donorName Donor's name
     * @param requestorName Requestor's name
     * @param requestorPhone Requestor's phone number
     * @param message Custom message
     * @param bloodType Required blood type
     * @param urgency Urgency level
     * @return Formatted SMS message
     */
    public String formatNotificationMessage(String donorName, String requestorName, 
                                           String requestorPhone, String message, 
                                           String bloodType, String urgency) {
        StringBuilder smsMessage = new StringBuilder();
        smsMessage.append("BloodBridge Alert\n\n");
        smsMessage.append("Hi ").append(donorName).append(",\n\n");
        smsMessage.append("You have a blood donation request:\n\n");
        smsMessage.append("Blood Type: ").append(bloodType).append("\n");
        smsMessage.append("Urgency: ").append(urgency).append("\n");
        smsMessage.append("From: ").append(requestorName);
        if (requestorPhone != null && !requestorPhone.isEmpty()) {
            smsMessage.append(" (").append(requestorPhone).append(")");
        }
        smsMessage.append("\n\n");
        if (message != null && !message.isEmpty()) {
            smsMessage.append("Message: ").append(message).append("\n\n");
        }
        smsMessage.append("Please contact if you can help.\n\n");
        smsMessage.append("Thank you!");
        
        return smsMessage.toString();
    }
    
    /**
     * Check if SMS is properly configured
     * @return Map with configuration status
     */
    public Map<String, Object> getSmsConfigurationStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", smsEnabled);
        status.put("useFreeGateway", useFreeGateway);
        status.put("hasMailSender", mailSender != null);
        status.put("hasFromEmail", fromEmail != null && !fromEmail.isEmpty());
        
        // Check Twilio configuration
        boolean hasTwilio = accountSid != null && !accountSid.isEmpty() && 
            !accountSid.equals("YOUR_TWILIO_ACCOUNT_SID") &&
            authToken != null && !authToken.isEmpty() && 
            !authToken.equals("YOUR_TWILIO_AUTH_TOKEN") &&
            twilioPhoneNumber != null && !twilioPhoneNumber.isEmpty() && 
            !twilioPhoneNumber.equals("YOUR_TWILIO_PHONE_NUMBER");
        
        status.put("hasTwilio", hasTwilio);
        
        // Free gateway is fully configured if email is set up
        boolean freeGatewayConfigured = useFreeGateway && 
            (Boolean) status.get("hasMailSender") && 
            (Boolean) status.get("hasFromEmail");
        
        // Fully configured if either free gateway OR Twilio is configured
        boolean fullyConfigured = smsEnabled && (freeGatewayConfigured || hasTwilio);
        status.put("fullyConfigured", fullyConfigured);
        status.put("freeGatewayConfigured", freeGatewayConfigured);
        
        if (!fullyConfigured) {
            List<String> missing = new ArrayList<>();
            if (!smsEnabled) {
                missing.add("SMS is disabled (set sms.enabled=true)");
            }
            if (useFreeGateway && !freeGatewayConfigured) {
                if (!(Boolean) status.get("hasMailSender")) {
                    missing.add("Mail sender not available (configure email first)");
                }
                if (!(Boolean) status.get("hasFromEmail")) {
                    missing.add("Sender email not configured (set spring.mail.username)");
                }
            }
            if (!useFreeGateway && !hasTwilio) {
                missing.add("Free gateway disabled and Twilio not configured");
                missing.add("Enable free gateway: sms.use.free.gateway=true OR configure Twilio");
            }
            status.put("missingConfig", missing);
        } else {
            if (freeGatewayConfigured) {
                status.put("method", "Free Email-to-SMS Gateway");
            } else if (hasTwilio) {
                status.put("method", "Twilio (Paid)");
            }
        }
        
        return status;
    }
}

