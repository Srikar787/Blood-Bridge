# n8n Integration Guide for BloodBridge Notifications

## Overview
The notification system supports two methods for sending notifications when a requestor contacts a donor:
1. **SMS via Twilio** - Direct SMS messages sent to donor's phone number (primary method)
2. **n8n Webhook** - Integration with n8n for automated notifications (email, SMS, WhatsApp, etc.)

## Backend Configuration

### 1. SMS Configuration (Twilio) - Primary Method

The system sends SMS notifications directly to the donor's phone number using Twilio.

#### Step 1: Get Twilio Credentials
1. Sign up for a Twilio account at https://www.twilio.com/
2. Get your Account SID and Auth Token from the Twilio Console
3. Purchase a Twilio phone number (required for sending SMS)

#### Step 2: Configure in application.properties
In `backend/src/main/resources/application.properties`, add your Twilio credentials:

```properties
# Enable SMS notifications
sms.enabled=true

# Twilio Account Configuration
twilio.account.sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth.token=YOUR_TWILIO_AUTH_TOKEN
twilio.phone.number=+1234567890  # Your Twilio phone number in E.164 format
```

#### Step 3: Phone Number Format
- Donor phone numbers should be in E.164 format (e.g., +1234567890)
- The system will automatically normalize phone numbers if needed
- Ensure donor records have valid phone numbers

#### SMS Message Format
The SMS sent to donors includes:
- BloodBridge Alert header
- Donor's name
- Blood type required
- Urgency level
- Requestor name and phone number
- Custom message from requestor

### 2. Set n8n Webhook URL (Optional)
In `backend/src/main/resources/application.properties`, add your n8n webhook URL:

```properties
n8n.webhook.url=https://your-n8n-instance.com/webhook/notify-donor
```

### 2. n8n Webhook Payload Format
When a notification is sent, the following payload is sent to your n8n webhook:

```json
{
  "donorId": "donor123",
  "donorName": "John Doe",
  "donorEmail": "john@example.com",
  "donorPhone": "+1234567890",
  "requestorName": "Jane Smith",
  "requestorEmail": "jane@example.com",
  "requestorPhone": "+1234567890",
  "message": "Hello John, I need O+ blood. Please contact me if you can help.",
  "bloodType": "O+",
  "urgency": "HIGH",
  "notificationId": "notif123"
}
```

## n8n Workflow Setup

### Step 1: Create Webhook Trigger
1. In n8n, create a new workflow
2. Add a **Webhook** node
3. Set HTTP Method to **POST**
4. Copy the webhook URL
5. Add it to `application.properties` as `n8n.webhook.url`

### Step 2: Process Notification Data
Add nodes to:
- Extract donor and requestor information
- Format notification message
- Send via email/SMS/WhatsApp/etc.

### Example n8n Workflow:
1. **Webhook** (receives notification)
2. **Function** (format message)
3. **Email** (send to donor)
4. **SMS** (optional - send SMS to donor)
5. **Slack/Discord** (optional - notify admin)

### Step 3: Email Template Example
```
Subject: Blood Donation Request - Urgent

Dear {{ $json.donorName }},

You have received a blood donation request:

Blood Type Required: {{ $json.bloodType }}
Urgency: {{ $json.urgency }}
Requestor: {{ $json.requestorName }}
Contact: {{ $json.requestorPhone }}

Message:
{{ $json.message }}

Please contact the requestor if you can help.

Thank you,
BloodBridge Team
```

## Testing

1. Start the backend server
2. Configure n8n webhook URL in `application.properties`
3. Log in as a requestor
4. View donors list or search for donors
5. Click "Send Notification" on a donor
6. Check n8n workflow execution logs

## Notification Flow

1. **SMS is sent first** (if enabled and donor has phone number)
   - SMS is sent directly to donor's phone via Twilio
   - Status updated to "SENT" if SMS succeeds

2. **n8n webhook is called** (if configured)
   - Additional notifications can be sent via n8n
   - Useful for email, WhatsApp, or other channels

3. **Status tracking**
   - "PENDING" - Notification created but not sent
   - "SENT" - Successfully sent via SMS or n8n
   - "READ" - Donor has viewed the notification
   - "RESPONDED" - Donor has responded

## Without Twilio/n8n

If SMS is disabled and n8n is not configured:
- Notifications are still saved in the database
- Status remains "PENDING"
- You can manually process notifications later
- Build a custom notification service
- Integrate with other notification services

## API Endpoints

- `POST /api/notifications/send` - Send notification to donor
- `GET /api/notifications/donor/{donorId}` - Get notifications for a donor
- `GET /api/notifications/my-notifications` - Get notifications sent by current user

## Database

Notifications are stored in MongoDB collection `notifications` with the following structure:
- donorId, donorEmail, donorName, donorPhone
- requestorId, requestorEmail, requestorName, requestorPhone
- message, bloodType, urgency
- status (PENDING, SENT, READ, RESPONDED)
- timestamps (createdAt, sentAt, readAt)


