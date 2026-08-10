package com.github.stanislawmalinski.crud_service.repositories;

import com.github.stanislawmalinski.crud_service.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Page<User> findByUsernameContaining(String username, Pageable pageable);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}