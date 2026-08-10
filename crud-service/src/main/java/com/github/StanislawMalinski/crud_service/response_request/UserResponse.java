package com.github.stanislawmalinski.crud_service.response_request;

import com.github.stanislawmalinski.crud_service.models.Role;
import com.github.stanislawmalinski.crud_service.models.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Date;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Long eloRating;
    private Date signedUpDate;
    private Date lastSeen;

    public static UserResponse from(User user){
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .eloRating(user.getEloRating())
                .signedUpDate(user.getSignedUpDate())
                .lastSeen(user.getLastSeen())
                .build();
    }

    public static Page<UserResponse> from(Page<User> users){
        return users.map(UserResponse::from);
    }

    public User getUser() {
        return User.builder()
                .id(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .role(this.getRole())
                .eloRating(this.getEloRating())
                .signedUpDate(this.getSignedUpDate())
                .lastSeen(this.getLastSeen())
                .build();
    }
}
