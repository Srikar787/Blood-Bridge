# SMS Notification Troubleshooting Guide

> **Note:** For a FREE alternative, use Email Notifications instead! See `FREE_EMAIL_SETUP.md` for details.
> Email notifications are completely free and don't require any paid services like Twilio.

## Common Issues and Solutions

### Issue 1: "I didn't get a notification"

**Possible Causes:**

1. **Twilio credentials not configured**
   - Check `backend/src/main/resources/application.properties`
   - Ensure you have real Twilio credentials (not placeholders):
     ```properties
     sms.enabled=true
     twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     twilio.auth.token=your_auth_token_here
     twilio.phone.number=+1234567890
     ```

2. **Donor phone number missing**
   - The donor profile must have a valid phone number
   - Check the donor record in the database
   - Phone number should be in E.164 format (e.g., +1234567890)

3. **SMS disabled**
   - Check if `sms.enabled=false` in application.properties
   - Set it to `sms.enabled=true`

4. **Twilio account issues**
   - Verify your Twilio account is active
   - Check if you have sufficient credits
   - Verify your Twilio phone number is verified and can send SMS

### How to Check SMS Configuration

1. **Check the API response**
   - When sending a notification, check the response for `smsConfig` and `smsWarning` fields
   - The response will tell you what's missing

2. **Use the diagnostic endpoint**
   - Call `GET /api/notifications/sms-status`
   - This will show you the current SMS configuration status

3. **Check backend logs**
   - Look for SMS-related log messages:
     - "SMS sent successfully" - SMS was sent
     - "Failed to send SMS notification" - SMS failed
     - "Cannot send SMS: Twilio credentials not configured" - Missing credentials
     - "Cannot send SMS: donor phone number is not available" - Missing phone number

### Setting Up Twilio

1. **Create a Twilio account**
   - Go to https://www.twilio.com/
   - Sign up for a free trial account
   - You'll get $15.50 in free credits

2. **Get your credentials**
   - Account SID: Found in Twilio Console dashboard
   - Auth Token: Found in Twilio Console dashboard (click to reveal)
   - Phone Number: Purchase a phone number from Twilio (required for sending SMS)

3. **Configure in application.properties**
   ```properties
   sms.enabled=true
   twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   twilio.auth.token=your_actual_auth_token
   twilio.phone.number=+1234567890
   ```

4. **Restart the backend server**
   - After updating application.properties, restart the Spring Boot application

### Testing SMS

1. **Check SMS status endpoint**
   ```bash
   curl http://localhost:8081/api/notifications/sms-status
   ```

2. **Send a test notification**
   - Use the notification modal in the frontend
   - Check the response for SMS status
   - Check backend logs for SMS sending attempts

3. **Verify in Twilio Console**
   - Go to Twilio Console > Monitor > Logs > Messaging
   - You should see SMS attempts and their status

### Phone Number Format

- **Correct**: `+1234567890` (E.164 format with country code)
- **Correct**: `1234567890` (will be auto-normalized to +11234567890 for US)
- **Incorrect**: `(123) 456-7890` (will be cleaned but may cause issues)

The system automatically normalizes phone numbers, but it's best to store them in E.164 format.

### Common Error Messages

| Error Message | Solution |
|--------------|----------|
| "SMS is disabled" | Set `sms.enabled=true` in application.properties |
| "Twilio credentials not configured" | Add real Twilio credentials (not placeholders) |
| "Donor phone number is not available" | Add phone number to donor profile |
| "Failed to send SMS" | Check Twilio account status, credits, and phone number verification |

### Debugging Steps

1. **Check application.properties**
   - Verify all Twilio settings are correct
   - Ensure no placeholder values remain

2. **Check donor data**
   - Verify donor has a phone number
   - Check phone number format

3. **Check backend logs**
   - Look for SMS-related error messages
   - Check for Twilio API errors

4. **Test Twilio credentials**
   - Try sending SMS directly via Twilio Console
   - Verify phone number is active

5. **Check network/firewall**
   - Ensure backend can reach Twilio API (api.twilio.com)
   - Check for proxy/firewall issues

### Alternative: Using n8n for SMS

If you prefer not to use Twilio directly, you can:
1. Set up n8n webhook
2. Configure n8n to send SMS using Twilio or another SMS provider
3. The notification will be sent to n8n, which then sends SMS

See `N8N_INTEGRATION_GUIDE.md` for details.

