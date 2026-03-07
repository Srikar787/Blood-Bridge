# API Code Snippets (For Interviews)

These are real snippets from the project to reference during interviews.

## Send Notification API (Backend - Spring Boot)
```java
// NotificationController.java
@PostMapping("/send")
public ResponseEntity<Map<String, Object>> sendNotification(
        @RequestBody NotificationRequest request,
        Authentication authentication) {
    String requestorEmail = authentication.getName();
    Notification notification = notificationService.sendNotification(requestorEmail, request);

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "Notification sent successfully to donor");
    response.put("notification", notification);
    return ResponseEntity.ok(response);
}
```

**What it does:** Authenticated user calls `/api/notifications/send` with a JSON body (`NotificationRequest`). The controller gets the caller’s email from the security context and delegates to `NotificationService`, which validates the donor, creates a `Notification` record, and triggers delivery (email/SMS/webhook based on config). Returns a JSON response with status and the saved notification.

## Donor Search API (Backend - Spring Boot)
```java
// DonorSearchController.java
@GetMapping("/search/eligibility-criteria")
public ResponseEntity<EligibilityCriteria> getEligibilityCriteria() {
    EligibilityCriteria criteria = eligibilityService.getEligibilityCriteria();
    return ResponseEntity.ok(criteria);
}
```

**What it does:** Simple GET endpoint to fetch donor eligibility criteria. Useful for frontend forms to know what fields/limits to show.

## Auth Login API (Backend - Spring Boot)
```java
// AuthController.java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    String token = authService.authenticateUser(request.getEmail(), request.getPassword());
    User user = authService.getUserByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    return ResponseEntity.ok(new AuthResponse(
        token,
        user.getEmail(),
        user.getName(),
        user.getRole(),
        "Login successful"
    ));
}
```

**What it does:** Validates credentials, issues a JWT, and returns user details plus a message. Frontend stores the token and sends it in `Authorization: Bearer <token>` for subsequent calls.





