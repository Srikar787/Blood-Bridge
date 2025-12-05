# n8n Workflow Setup for BloodBridge

This directory contains the n8n workflow configuration for automating donor notifications and other processes.

## Prerequisites

- n8n installed (via npm, Docker, or n8n Cloud)
- Backend API running on `http://localhost:8080`

## Installation

### Option 1: Using npm
```bash
npm install n8n -g
n8n start
```

### Option 2: Using Docker
```bash
docker run -it --rm \
  --name n8n \
  -p 5678:5678 \
  -v ~/.n8n:/home/node/.n8n \
  n8nio/n8n
```

### Option 3: Using n8n Cloud
Sign up at https://n8n.io/cloud and import the workflow.

## Importing the Workflow

1. Open n8n (usually at http://localhost:5678)
2. Click on "Workflows" in the sidebar
3. Click "Import from File" or "Import from URL"
4. Select the `workflow.json` file from this directory
5. The workflow will be imported with all nodes configured

## Workflow Description

The included workflow demonstrates:
- **Webhook Trigger**: Receives POST requests when a new donor is registered
- **HTTP Request**: Fetches all donors from the backend API
- **Conditional Logic**: Checks if the donor has a universal blood type (O-)
- **Email Notification**: Sends a welcome email to new donors
- **Response**: Returns a success message to the webhook caller

## Customization

You can customize this workflow to:
- Send SMS notifications via Twilio
- Post to Slack/Discord channels
- Update external databases
- Trigger other automation workflows
- Schedule periodic donor reminders

## Testing the Workflow

1. Start n8n and import the workflow
2. Activate the workflow in n8n
3. Test the webhook endpoint:
```bash
curl -X POST http://localhost:5678/webhook/donor-registered \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","bloodType":"O-"}'
```

## Integration with Backend

To integrate this workflow with your Spring Boot backend, you can add a webhook call in your `DonorController` after creating a new donor:

```java
@PostMapping
public ResponseEntity<Donor> createDonor(@RequestBody Donor donor) {
    Donor savedDonor = donorRepository.save(donor);
    
    // Trigger n8n webhook
    restTemplate.postForObject(
        "http://localhost:5678/webhook/donor-registered",
        savedDonor,
        String.class
    );
    
    return ResponseEntity.status(HttpStatus.CREATED).body(savedDonor);
}
```

## Additional Resources

- [n8n Documentation](https://docs.n8n.io/)
- [n8n Community Forum](https://community.n8n.io/)
- [n8n Workflow Examples](https://n8n.io/workflows/)



