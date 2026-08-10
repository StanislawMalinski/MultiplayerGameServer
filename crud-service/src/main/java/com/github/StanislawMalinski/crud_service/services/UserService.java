package com.github.stanislawmalinski.crud_service.services;

import com.github.stanislawmalinski.crud_service.models.Role;
import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response_request.ExpUsernameAlreadyExists;
import com.github.stanislawmalinski.crud_service.response_request.ExpUserDoesNotExists;
import com.github.stanislawmalinski.crud_service.response_request.UserResponse;
import com.github.stanislawmalinski.crud_service.response_request.ExpUserWithThisEmailAlreadyExists;
import com.github.stanislawmalinski.crud_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository repo;

    public UserResponse createNewUser(User user) throws ExpUserWithThisEmailAlreadyExists, ExpUsernameAlreadyExists {
        if (repo.existsByEmail(user.getEmail())) throw new ExpUserWithThisEmailAlreadyExists();
        if (repo.existsByUsername(user.getUsername())) throw new ExpUsernameAlreadyExists();
        user.setId(null);
        user.setEloRating(1500L);
        user.setSignedUpDate(new Date());
        user.setLastSeen(new Date());
        user.setRole(Role.REGULAR_USER);
        return UserResponse.from(repo.save(user));
    }

    public User getUserByEitherUsernameOrUsername(String user_mail){
        if (user_mail.contains("@"))
            return getUserByEmail(user_mail);
        return getUserByUsername(user_mail);
    }

    public User getUserByUsername(String username){
        return repo.findByUsername(username);
    }

    public Page<User> getUserByUsername(String username, Pageable pageable) {
        return repo.findByUsernameContaining(username, pageable);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return repo.findById(id);
    }

    public UserResponse updateUser(User newUser) throws ExpUserDoesNotExists {
        Optional<User> oldUser = getUserById(newUser.getId());
        if (oldUser.isEmpty()) throw new ExpUserDoesNotExists();
        User user = oldUser.get();

        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());

        return UserResponse.from(repo.save(user));
    }

    public void deleteUserById(Long id) throws ExpUserDoesNotExists {
        Optional<User> user = getUserById(id);
        if (user.isEmpty()) throw new ExpUserDoesNotExists();
        repo.delete(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username);
    }
}
