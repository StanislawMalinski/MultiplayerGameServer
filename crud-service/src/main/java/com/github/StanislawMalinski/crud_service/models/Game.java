package com.github.stanislawmalinski.crud_service.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String gameName;
}
