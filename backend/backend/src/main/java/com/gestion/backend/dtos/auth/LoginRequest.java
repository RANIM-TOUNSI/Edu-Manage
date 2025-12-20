package com.gestion.backend.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username/Email is required")
    private String email; // Using 'email' as the username field as per prompt

    @NotBlank(message = "Password is required")
    private String password;
}
