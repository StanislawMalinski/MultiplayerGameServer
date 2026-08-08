package com.github.stanislawmalinski.crud_service.repositories;

import com.github.stanislawmalinski.crud_service.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByNickName(String nickName);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
}