package com.github.stanislawmalinski.crud_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@Table(
    name = "Users",
    uniqueConstraints = @UniqueConstraint(columnNames={"nick_name"})
)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String nickName;
    private String email;
    private String pass;

    private Role role;
    private Long eloRating;

    private Date signedUpDate;
    private Date lastSeen;

    @Override
    public String toString(){
        return "" + nickName + "(" + email + ")";
    }
}