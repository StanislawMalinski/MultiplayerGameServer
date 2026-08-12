package com.github.stanislawmalinski.crud_service.services;

import com.github.stanislawmalinski.crud_service.models.Match;
import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.repositories.GameRepository;
import com.github.stanislawmalinski.crud_service.repositories.MatchRepository;
import com.github.stanislawmalinski.crud_service.repositories.UserRepository;
import com.github.stanislawmalinski.crud_service.response_request.ExpMatchDoesNotExists;
import com.github.stanislawmalinski.crud_service.response_request.ExpUserDoesNotExists;
import com.github.stanislawmalinski.crud_service.response_request.MatchDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class MatchService {
    private MatchRepository repo;
    private GameRepository gameRepository;
    private UserRepository userRepository;

    public Match getMatchById(Long id) throws ExpMatchDoesNotExists {
        Optional<Match> match = repo.findById(id);
        if (match.isEmpty()) throw new ExpMatchDoesNotExists();
        return match.get();
    }

    public Page<Match> getMatchesForUser(Long userId, int page) throws ExpUserDoesNotExists {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new ExpUserDoesNotExists();
        Pageable pageable = PageRequest.of(page, 10);
        return repo.getMatchesByPlayer(user.get().getUsername(), pageable);
    }

    private Match parseToMatch(MatchDTO matchDto){
        Match match = new Match();
        match.setPlayer(userRepository.getReferenceById(matchDto.playerId()));
        match.setOpponent(userRepository.getReferenceById(matchDto.opponentId()));
        match.setEloDifference(matchDto.eloDifference());
        match.setTimeFormat(matchDto.timeFormat());
        match.setGame(gameRepository.getReferenceById(matchDto.gameId()));
        // TODO Match reference
        return match;
    }

    public MatchDTO persistMatch(MatchDTO match) {
        Match res = repo.save(parseToMatch(match));
        Optional<User> userOpt = userRepository.findById(res.getPlayer().getId());
        assert userOpt.isPresent();
        User user = userOpt.get();
        user.setEloRating(user.getEloRating() + match.eloDifference());
        userRepository.save(user);
        return MatchDTO.toDto(res);
    }
}
