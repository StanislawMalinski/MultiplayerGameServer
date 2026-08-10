package com.github.stanislawmalinski.crud_service.response_request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private final String username; // Either username or email
    private final String password;
}
