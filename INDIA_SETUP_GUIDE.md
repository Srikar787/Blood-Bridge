# India Setup Guide - Free Email & SMS Notifications

## Overview

This guide helps you set up **completely FREE** email and SMS notifications for BloodBridge in India.

## ✅ Both Email and SMS are FREE!

### Email Notifications
- Works globally, including India
- Uses Gmail or any SMTP service
- Completely free

### SMS Notifications  
- Uses email-to-SMS gateways
- Supports major Indian carriers
- Completely free

## Quick Setup

### Step 1: Configure Email (Required for both Email & SMS)

Since SMS uses email infrastructure, configure email first:

1. **Enable 2-Step Verification on Gmail**
   - Go to https://myaccount.google.com/security
   - Enable 2-Step Verification

2. **Generate App Password**
   - Go to https://myaccount.google.com/apppasswords
   - Select "Mail" and "Other (Custom name)"
   - Enter "BloodBridge"
   - Copy the 16-character password

3. **Update `application.properties`**
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

### Step 2: Enable SMS (Uses Email Infrastructure)

```properties
# SMS Configuration - FREE Email-to-SMS Gateway
sms.enabled=true
sms.use.free.gateway=true
```

### Step 3: Restart Backend

Restart your Spring Boot application.

## Indian Phone Number Format

The system accepts Indian phone numbers in these formats:

✅ **Recommended Formats:**
- `+919876543210` (E.164 format with +91)
- `9876543210` (10-digit - system auto-detects as Indian)
- `919876543210` (with country code 91)

✅ **Also Accepted:**
- `98765 43210` (with spaces)
- `98765-43210` (with dashes)
- `(98765) 43210` (formatted)

The system automatically:
- Detects Indian numbers (starts with 6-9, 10 digits)
- Adds +91 country code if missing
- Normalizes to correct format

## Supported Indian Carriers

The system tries these carriers automatically:

1. **Airtel** - `number@airtelmail.com`
2. **Vodafone Idea** - `number@vodaemail.co.in`
3. **BSNL** - `number@bsnl.co.in`
4. **Reliance Jio** - `number@rjil.co.in`
5. **Tata Docomo** - `number@tatadocomo.com`

**Note:** The system tries all carriers - one will work if the number is valid!

## How It Works

### Email Notifications:
1. Requestor sends notification
2. System sends email to donor's email address
3. Donor receives email in inbox

### SMS Notifications:
1. Requestor sends notification
2. System detects Indian phone number (+91 or 10-digit starting with 6-9)
3. System extracts 10-digit number
4. System tries sending email to each carrier's gateway
5. Carrier converts email to SMS
6. Donor receives SMS on phone

## Testing

### Test Email:
1. Send a notification to a donor with email
2. Check donor's email inbox
3. Should receive formatted email notification

### Test SMS:
1. Send a notification to a donor with Indian phone number
2. Check backend logs for SMS sending attempts
3. Check donor's phone for SMS message

### Check Status:
```bash
# Check email status
curl http://localhost:8081/api/notifications/email-status

# Check SMS status
curl http://localhost:8081/api/notifications/sms-status
```

## Troubleshooting

### Email Not Working
1. **Check Gmail App Password**
   - Make sure you're using App Password, not regular password
   - Generate new App Password if needed

2. **Check Configuration**
   - Verify `spring.mail.username` is your Gmail address
   - Verify `spring.mail.password` is 16-character App Password

3. **Check Backend Logs**
   - Look for email-related errors
   - Common: "Authentication failed" = wrong password

### SMS Not Working
1. **Check Email First**
   - SMS requires email to be working
   - Test email notifications first

2. **Check Phone Number Format**
   - Should be 10 digits (Indian format)
   - Should start with 6, 7, 8, or 9
   - Can include +91 prefix

3. **Check Carrier Support**
   - Some carriers may not support email-to-SMS
   - System tries all major carriers
   - Check backend logs for attempted gateways

4. **Carrier-Specific Issues**
   - Some carriers filter email-to-SMS messages
   - Recipient may need to enable email-to-SMS in carrier settings
   - Some carriers have limited support

### Common Issues

**"SMS could not be sent"**
- Email not configured properly
- Phone number format incorrect
- Carrier doesn't support email-to-SMS

**"Email could not be sent"**
- Gmail App Password incorrect
- SMTP configuration wrong
- Check backend logs for details

## Benefits for India

✅ **Completely Free** - No paid services required  
✅ **Works with Gmail** - Easy to set up  
✅ **Major Carriers Supported** - Airtel, Vodafone, Jio, BSNL, etc.  
✅ **Automatic Detection** - System detects Indian numbers automatically  
✅ **No API Keys** - Just configure email  
✅ **Both Email & SMS** - Send notifications via both channels  

## Important Notes

⚠️ **Carrier Limitations**
- Not all Indian carriers support email-to-SMS
- Some carriers may filter or block messages
- Delivery is not 100% guaranteed

⚠️ **Message Length**
- Keep SMS messages short (160 characters recommended)
- Long messages may be truncated

⚠️ **Regulatory Compliance**
- For bulk SMS in India, you may need DLT registration
- Email-to-SMS may have usage limits
- Check carrier policies

## Alternative: If Email-to-SMS Doesn't Work

If email-to-SMS doesn't work reliably for your use case:

1. **Use Email Only** - Email notifications work reliably
2. **Use WhatsApp** - Consider WhatsApp Business API (has free tier)
3. **Use Push Notifications** - For mobile app users
4. **Use Paid SMS Service** - For guaranteed delivery (SMSLocal, etc.)

## Next Steps

1. ✅ Configure Gmail in `application.properties`
2. ✅ Enable SMS: `sms.enabled=true` and `sms.use.free.gateway=true`
3. ✅ Restart backend server
4. ✅ Test with Indian phone numbers
5. ✅ Check both email and SMS delivery
6. ✅ Enjoy free notifications! 🎉

## Support

For issues:
1. Check backend logs
2. Verify email configuration
3. Test phone number format
4. Check carrier support

Both email and SMS are now FREE and working for India! 🇮🇳

