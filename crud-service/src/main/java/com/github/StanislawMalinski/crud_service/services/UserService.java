package com.github.stanislawmalinski.crud_service.services;

import com.github.stanislawmalinski.crud_service.models.Role;
import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response.ExpNicknameAlreadyExists;
import com.github.stanislawmalinski.crud_service.response.ExpUserDoesNotExists;
import com.github.stanislawmalinski.crud_service.response.UserResponse;
import com.github.stanislawmalinski.crud_service.response.ExpUserWithThisEmailAlreadyExists;
import com.github.stanislawmalinski.crud_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private static Logger log = LoggerFactory.getLogger(UserService.class);

    UserRepository repo;

    public UserResponse createNewUser(User user) throws ExpUserWithThisEmailAlreadyExists, ExpNicknameAlreadyExists {
        if (repo.existsByEmail(user.getEmail())) throw new ExpUserWithThisEmailAlreadyExists();
        if (repo.existsByNickName(user.getNickName())) throw new ExpNicknameAlreadyExists();
        user.setId(null);
        user.setEloRating(1500L);
        user.setSignedUpDate(new Date());
        user.setLastSeen(new Date());
        user.setRole(Role.RegularUser);
        return UserResponse.from(repo.save(user));
    }

    public Page<User> getUserByNickName(String nickName, Pageable pageable) {
        return repo.findByNickNameContaining(nickName, pageable);
    }

    public Optional<User> getUserById(Long id) {
        return repo.findById(id);
    }

    public UserResponse updateUser(User newUser) throws ExpUserDoesNotExists {
        Optional<User> oldUser = getUserById(newUser.getId());
        if (oldUser.isEmpty()) throw new ExpUserDoesNotExists();
        User user = oldUser.get();

        user.setNickName(newUser.getNickName());
        user.setPass(newUser.getPass());

        return UserResponse.from(repo.save(user));
    }

    public void deleteUserById(Long id) throws ExpUserDoesNotExists {
        Optional<User> user = getUserById(id);
        if (user.isEmpty()) throw new ExpUserDoesNotExists();
        repo.delete(user.get());
    }
}
