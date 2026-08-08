package com.github.stanislawmalinski.crud_service.services;

import com.github.stanislawmalinski.crud_service.models.Role;
import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response.NicknameAlreadyExistsExp;
import com.github.stanislawmalinski.crud_service.response.UserWithThisEmailAlreadyExistsExp;
import com.github.stanislawmalinski.crud_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private static Logger log = LoggerFactory.getLogger(UserService.class);

    UserRepository repo;

    public User createNewUser(User user) throws UserWithThisEmailAlreadyExistsExp, NicknameAlreadyExistsExp {
        if (repo.existsByEmail(user.getEmail())) throw new UserWithThisEmailAlreadyExistsExp();
        if (repo.existsByNickName(user.getNickName())) throw new NicknameAlreadyExistsExp();
        user.setEloRating(1500L);
        user.setSignedUpDate(new Date());
        user.setLastSeen(new Date());
        user.setRole(Role.RegularUser);
        return repo.save(user);
    }

    public List<User> getUserByNickName(String nickName) {
        return repo.findByNickName(nickName);
    }
}
