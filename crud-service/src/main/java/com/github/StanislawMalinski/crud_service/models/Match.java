package com.github.stanislawmalinski.crud_service.models;

import com.github.stanislawmalinski.crud_service.response_request.MatchDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
@Entity
@Table(name="Matches")
public class Match {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User player;
    @ManyToOne(fetch = FetchType.LAZY)
    private User opponent;
    private int eloDifference;
    @OneToOne
    private MatchPlayout matchReference;
    private String timeFormat;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Match setUpForNewSave(){
        this.id = null;
        return this;
    }
}
