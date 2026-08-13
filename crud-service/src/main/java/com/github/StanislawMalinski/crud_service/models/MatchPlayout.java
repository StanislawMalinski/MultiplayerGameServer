package com.github.stanislawmalinski.crud_service.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="MatchPlayouts")
public class MatchPlayout {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String playout;
}
