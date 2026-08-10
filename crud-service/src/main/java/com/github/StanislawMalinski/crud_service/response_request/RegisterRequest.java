package com.github.stanislawmalinski.crud_service.response_request;

import com.github.stanislawmalinski.crud_service.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private final String username;
    private final String email;
    private final String password;

    public User toUser() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .build();
    }
}
