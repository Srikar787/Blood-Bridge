# Free SMS Notification Setup Guide

## Overview

BloodBridge now supports **FREE SMS notifications** using email-to-SMS gateways! This is completely free and uses your existing email infrastructure.

**Supports both US and India phone numbers!**

## How It Works

Email-to-SMS gateways allow you to send SMS messages by sending an email to a special address format. Carriers provide this service for free:

### US Carriers:
- **AT&T**: `phonenumber@txt.att.net`
- **Verizon**: `phonenumber@vtext.com`
- **T-Mobile**: `phonenumber@tmomail.net`
- **Sprint**: `phonenumber@messaging.sprintpcs.com`
- **US Cellular**: `phonenumber@email.uscc.net`
- **Cricket**: `phonenumber@sms.cricketwireless.net`
- **Boost Mobile**: `phonenumber@sms.myboostmobile.com`
- **Virgin Mobile**: `phonenumber@vmobl.com`

### Indian Carriers:
- **Airtel**: `phonenumber@airtelmail.com`
- **Vodafone Idea**: `phonenumber@vodaemail.co.in`
- **BSNL**: `phonenumber@bsnl.co.in`
- **Reliance Jio**: `phonenumber@rjil.co.in`
- **Tata Docomo**: `phonenumber@tatadocomo.com`

The system automatically detects the country and tries multiple carriers - one will work if the phone number is valid!

## Setup

### Step 1: Configure Email (Required)

Free SMS uses your email configuration. Make sure email is set up first:

1. Follow the **FREE_EMAIL_SETUP.md** guide to configure Gmail or another SMTP service
2. Ensure `spring.mail.username` and `spring.mail.password` are configured

### Step 2: Enable Free SMS Gateway

In `backend/src/main/resources/application.properties`:

```properties
# Enable SMS notifications
sms.enabled=true

# Use FREE email-to-SMS gateway (recommended!)
sms.use.free.gateway=true
```

That's it! No additional configuration needed.

### Step 3: Restart Backend

Restart your Spring Boot application for changes to take effect.

## How It Works

1. When a notification is sent, the system extracts the phone number
2. It normalizes the number to 10-digit US format
3. It tries sending an email to each carrier's gateway
4. The first successful send completes the SMS delivery
5. The carrier converts the email to SMS and delivers it to the phone

## Phone Number Format

The system accepts phone numbers in various formats:

### US Numbers:
- `+1234567890` (E.164 format)
- `1234567890` (10-digit)
- `(123) 456-7890` (formatted)
- `1-123-456-7890` (with country code)

### Indian Numbers:
- `+919876543210` (E.164 format with +91)
- `9876543210` (10-digit - will auto-detect as Indian)
- `919876543210` (with country code 91)

The system automatically detects the country and normalizes them to the correct format.

## Testing

### Method 1: Check SMS Status
```bash
curl http://localhost:8081/api/notifications/sms-status
```

Expected response:
```json
{
  "enabled": true,
  "useFreeGateway": true,
  "hasMailSender": true,
  "hasFromEmail": true,
  "freeGatewayConfigured": true,
  "fullyConfigured": true,
  "method": "Free Email-to-SMS Gateway"
}
```

### Method 2: Send Test Notification
1. Log in as a requestor
2. Find a donor with a phone number
3. Click "Send Notification"
4. Check the response - it should say SMS was sent
5. Check the donor's phone for the SMS message

## Benefits

✅ **Completely Free** - No paid services required  
✅ **Uses Existing Email** - Works with your email configuration  
✅ **No API Keys** - No need to sign up for SMS services  
✅ **Automatic Carrier Detection** - Tries multiple carriers automatically  
✅ **Works with Major Carriers** - AT&T, Verizon, T-Mobile, Sprint, etc.  

## Limitations

⚠️ **US and India Only** - Currently supports US and Indian carriers  
⚠️ **Carrier Detection** - System tries all carriers; one will work  
⚠️ **Message Length** - SMS messages should be kept short (160 characters recommended)  
⚠️ **Delivery Not Guaranteed** - Some carriers may block or filter messages  
⚠️ **Indian Carrier Support** - Some Indian carriers may have limited email-to-SMS support  

## Troubleshooting

### "SMS could not be sent"
1. **Check Email Configuration**
   - Free SMS requires email to be configured
   - Verify `spring.mail.username` and `spring.mail.password` are set
   - Test email sending first

2. **Check Phone Number Format**
   - Ensure donor has a valid US phone number
   - Phone number should be 10 digits (or 11 with country code)

3. **Check Backend Logs**
   - Look for SMS-related error messages
   - Check if email-to-SMS gateway attempts are being made

### SMS Not Received
1. **Carrier Compatibility**
   - The system tries all major carriers
   - If the number is with a smaller carrier, it might not work
   - Check if the phone number's carrier is in the supported list

2. **Carrier Filtering**
   - Some carriers filter or block email-to-SMS messages
   - The recipient may need to enable email-to-SMS in their carrier settings

3. **Message Format**
   - Very long messages might be truncated
   - Keep messages concise

### "Mail sender not available"
- Configure email first (see FREE_EMAIL_SETUP.md)
- Free SMS requires email infrastructure

## Fallback to Twilio (Optional)

If free gateway fails and you have Twilio configured, the system will automatically try Twilio:

```properties
sms.enabled=true
sms.use.free.gateway=true  # Try free first
# Twilio will be used as fallback if configured
twilio.account.sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth.token=YOUR_TWILIO_AUTH_TOKEN
twilio.phone.number=+1234567890
```

## Comparison: Free Gateway vs Twilio

| Feature | Free Email-to-SMS Gateway | Twilio |
|---------|---------------------------|--------|
| Cost | **FREE** | Paid |
| Setup | Just configure email | Requires account & phone number |
| US Carriers | ✅ All major carriers | ✅ All carriers worldwide |
| International | ❌ US only | ✅ Worldwide |
| Delivery Guarantee | ⚠️ Not guaranteed | ✅ Guaranteed |
| Message Length | ⚠️ Limited | ✅ Full support |
| API Keys | ❌ Not needed | ✅ Required |

## Best Practice

For most use cases, **free email-to-SMS gateway is recommended**:
- It's completely free
- Easy to set up (just configure email)
- Works with all major US carriers
- No API keys or paid services needed

Use Twilio only if you need:
- International SMS
- Guaranteed delivery
- Delivery receipts
- Higher message limits

## Next Steps

1. ✅ Configure email (see FREE_EMAIL_SETUP.md)
2. ✅ Set `sms.enabled=true` and `sms.use.free.gateway=true`
3. ✅ Restart backend server
4. ✅ Test by sending a notification
5. ✅ Check donor's phone for SMS
6. ✅ Enjoy free SMS notifications! 🎉

## Technical Details

The system:
1. Normalizes phone number to 10-digit US format
2. Tries each carrier gateway in order
3. Sends email via your configured SMTP
4. First successful send completes the SMS
5. Carrier converts email to SMS automatically

This is a well-established method used by many applications for free SMS delivery!

