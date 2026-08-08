package com.github.stanislawmalinski.crud_service.controllers;

import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response.NicknameAlreadyExistsExp;
import com.github.stanislawmalinski.crud_service.response.UserWithThisEmailAlreadyExistsExp;
import com.github.stanislawmalinski.crud_service.response.Response;
import com.github.stanislawmalinski.crud_service.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/")
public class UserController {
    UserService service;

    @GetMapping(path = "/{nickName}", produces = "application/json")
    public ResponseEntity<Response<List<User>>> getUserByName(@PathVariable String nickName){
        List<User> users = service.getUserByNickName(nickName);
        Response<List<User>> res = new Response<>(
                "ok",
                null,
                users,
                null
        );
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity<Response<User>> createNewUser(@RequestBody User user){
        Response<User> resp = new Response<>();
        resp.setData(user);

        HttpStatus code;
        try {
            User newUser = service.createNewUser(user);
            resp.setData(newUser);
            code = HttpStatus.CREATED;
        } catch (UserWithThisEmailAlreadyExistsExp | NicknameAlreadyExistsExp e){
            resp.setMessage(e.getMessage());
            resp.setStatus(Response.FAILED);
            code = HttpStatus.CONFLICT;
        }
        return ResponseEntity
                .status(code)
                .body(resp);
    }
}
