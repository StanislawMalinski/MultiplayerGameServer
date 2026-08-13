package com.github.stanislawmalinski.crud_service.repositories;

import com.github.stanislawmalinski.crud_service.models.MatchPlayout;
import org.springframework.data.repository.CrudRepository;

public interface MatchPlayoutRepository extends CrudRepository<MatchPlayout, Long> {
    MatchPlayout getReferenceById(Long Id);
}
