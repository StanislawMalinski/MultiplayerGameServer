package com.github.stanislawmalinski.crud_service.repositories;

import com.github.stanislawmalinski.crud_service.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
    Game getReferenceById(Long id);
}
