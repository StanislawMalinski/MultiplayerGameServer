package com.github.stanislawmalinski.crud_service.response_request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private final String token;
    private final String username;

    public AuthResponse(){
        this.token = null;
        this.username = null;
    }
}
