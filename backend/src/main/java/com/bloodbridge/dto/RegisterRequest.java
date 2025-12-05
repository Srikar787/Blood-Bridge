package com.bloodbridge.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String role; // "DONOR" or "REQUESTOR"
    private String phone;
}

