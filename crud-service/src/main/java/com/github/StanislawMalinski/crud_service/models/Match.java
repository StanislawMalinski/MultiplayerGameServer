package com.github.stanislawmalinski.crud_service.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
@Table(name="Matches")
public class Match {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_nick_name")
    private User player;
    @ManyToOne
    @JoinColumn(name = "opponent_nick_name")
    private User opponent;
    private int eloDifference;
    @OneToOne
    private MatchPlayout matchReference;
}
