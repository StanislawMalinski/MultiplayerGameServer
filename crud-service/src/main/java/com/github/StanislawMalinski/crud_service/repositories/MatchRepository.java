package com.github.stanislawmalinski.crud_service.repositories;

import com.github.stanislawmalinski.crud_service.models.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Long> {
    Page<Match> getMatchesByPlayerId(Long id, Pageable pageable);
}
