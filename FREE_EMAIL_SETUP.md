# Free Email Notification Setup Guide

## Overview

BloodBridge now supports **FREE email notifications** using Gmail or any SMTP service. No paid services required!

## Quick Setup (Gmail - Recommended)

### Step 1: Enable 2-Factor Authentication
1. Go to https://myaccount.google.com/security
2. Enable 2-Step Verification if not already enabled

### Step 2: Generate App Password
1. Go to https://myaccount.google.com/apppasswords
2. Select "Mail" and "Other (Custom name)"
3. Enter "BloodBridge" as the name
4. Click "Generate"
5. Copy the 16-character password (you'll need this)

### Step 3: Configure application.properties
Edit `backend/src/main/resources/application.properties`:

```properties
# Email Configuration (FREE)
email.enabled=true
email.from.name=BloodBridge

# Gmail SMTP Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-16-character-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

**Important:** 
- Use your Gmail address for `spring.mail.username`
- Use the 16-character App Password (NOT your regular Gmail password)
- The App Password looks like: `abcd efgh ijkl mnop`

### Step 4: Restart Backend
Restart your Spring Boot application for changes to take effect.

## Alternative Free SMTP Services

### Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Yahoo Mail
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Other Free SMTP Services
- **SendGrid** - Free tier: 100 emails/day
- **Mailgun** - Free tier: 5,000 emails/month
- **Amazon SES** - Free tier: 62,000 emails/month (requires AWS account)

## Testing Email Configuration

### Method 1: Check Status Endpoint
```bash
curl http://localhost:8081/api/notifications/email-status
```

Expected response:
```json
{
  "enabled": true,
  "hasFromEmail": true,
  "hasMailSender": true,
  "fullyConfigured": true
}
```

### Method 2: Send Test Notification
1. Log in as a requestor
2. Find a donor with an email address
3. Click "Send Notification"
4. Check the response - it should say "Notification sent successfully via email"
5. Check the donor's email inbox

## Email Format

Donors will receive an email with:
- Subject: "BloodBridge Alert: Blood Donation Request - [URGENCY]"
- Professional formatted message including:
  - Blood type required
  - Urgency level
  - Requestor contact information
  - Custom message from requestor

## Troubleshooting

### "Email could not be sent"
1. **Check Gmail App Password**
   - Make sure you're using the App Password, not your regular password
   - Generate a new App Password if needed

2. **Check Gmail Security**
   - Ensure "Less secure app access" is NOT enabled (use App Password instead)
   - Verify 2-Step Verification is enabled

3. **Check Configuration**
   - Verify `spring.mail.username` matches your Gmail address
   - Verify `spring.mail.password` is the 16-character App Password
   - Check for typos in application.properties

4. **Check Backend Logs**
   - Look for email-related error messages
   - Common errors:
     - "Authentication failed" - Wrong password or App Password
     - "Connection refused" - Wrong SMTP host/port
     - "Timeout" - Firewall blocking port 587

### "Donor email address is not available"
- The donor profile must have a valid email address
- Check the donor record in the database

### Email Not Received
1. Check spam/junk folder
2. Verify donor email address is correct
3. Check backend logs for sending errors
4. Try sending to your own email first to test

## Disabling SMS (Optional)

Since email is free and works well, you can disable SMS:

```properties
sms.enabled=false
```

This way, only free email notifications will be sent.

## Benefits of Email Notifications

✅ **Completely Free** - No paid services required  
✅ **Reliable** - Works with Gmail, Outlook, Yahoo, etc.  
✅ **Professional** - Formatted email messages  
✅ **No Limits** - Send as many as you need (within SMTP provider limits)  
✅ **Easy Setup** - Just configure SMTP settings  

## Next Steps

1. Configure email in `application.properties`
2. Restart backend server
3. Test by sending a notification
4. Check donor's email inbox
5. Enjoy free notifications! 🎉

