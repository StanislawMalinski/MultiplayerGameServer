package com.github.stanislawmalinski.crud_service.controllers;

import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response.*;
import com.github.stanislawmalinski.crud_service.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/")
public class UserController {
    UserService service;

    @GetMapping(path = "/id/{id}", produces = "application/json")
    public ResponseEntity<@NonNull Response<UserResponse>> getUserById(@PathVariable Long id){
        Optional<User> user = service.getUserById(id);
        Response<UserResponse> resp = new Response<>();
        if (user.isEmpty()) {
            resp.setStatus(Response.FAILED);
            resp.setMessage("User with given id does not exists.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        resp.setStatus(Response.OK);
        resp.setData(user.map(UserResponse::from).get());

        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/nickname/{nickName}", produces = "application/json")
    public ResponseEntity<@NonNull Response<Page<UserResponse>>> getUserByName(@PathVariable String nickName){
        return getUserByName(nickName, 0);
    }

    @GetMapping(path = "/nickname/{nickName}/{page}", produces = "application/json")
    public ResponseEntity<@NonNull Response<Page<UserResponse>>> getUserByName(@PathVariable String nickName, @PathVariable int page){
        Pageable p = PageRequest.of(page,10);
        Page<User> users = service.getUserByNickName(nickName, p);
        Response<Page<UserResponse>> res = new Response<>(
                "ok",
                null,
                UserResponse.from(users),
                null
        );
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<@NonNull Response<UserResponse>> createNewUser(@RequestBody User user){
        Response<UserResponse> resp = Response.of(UserResponse.from(user));

        HttpStatus code;
        try {
            UserResponse newUser = service.createNewUser(user);
            resp.setData(newUser);
            code = HttpStatus.CREATED;
        } catch (ExpUserWithThisEmailAlreadyExists | ExpNicknameAlreadyExists e){
            resp.setMessage(e.getMessage());
            resp.setStatus(Response.FAILED);
            code = HttpStatus.CONFLICT;
        }
        return ResponseEntity
                .status(code)
                .body(resp);
    }

    @PostMapping(path = "/update", produces = "application/json")
    public ResponseEntity<@NonNull Response<UserResponse>> updateUser(@RequestBody User user){
        Response<UserResponse> resp = Response.of(UserResponse.from(user));

        try {
            UserResponse data = service.updateUser(user);
            resp.setData(data);
            resp.setStatus(Response.OK);
        } catch (ExpUserDoesNotExists e) {
            resp.setStatus(Response.FAILED);
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<@NonNull Response<UserResponse>> deleteUser(@PathVariable Long id){
        Response<UserResponse> resp = new Response<>();

        try {
            service.deleteUserById(id);
        } catch (ExpUserDoesNotExists e) {
            resp.setStatus(Response.FAILED);
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
