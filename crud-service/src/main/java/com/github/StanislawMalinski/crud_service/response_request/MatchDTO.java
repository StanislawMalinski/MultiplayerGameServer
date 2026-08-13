package com.github.stanislawmalinski.crud_service.response_request;

import com.github.stanislawmalinski.crud_service.models.Match;

public record MatchDTO( Long matchId,
                        Long playerId,
                        Long opponentId,
                        int eloDifference,
                        String timeFormat,
                        Long gameId,
                        Long matchPlayout){

    public static MatchDTO toDto(Match match) {
        return new MatchDTO(
            match.getId(),
            match.getPlayer().getId(),
            match.getOpponent().getId(),
            match.getEloDifference(),
            match.getTimeFormat(),
            match.getGame().getId(),
            match.getMatchPlayout().getId()
        );
    }
}
