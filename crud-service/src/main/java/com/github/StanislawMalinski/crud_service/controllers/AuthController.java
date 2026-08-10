package com.github.stanislawmalinski.crud_service.controllers;

import com.github.stanislawmalinski.crud_service.models.User;
import com.github.stanislawmalinski.crud_service.response_request.*;
import com.github.stanislawmalinski.crud_service.services.UserService;
import com.github.stanislawmalinski.crud_service.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final String HEADER_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        Response<AuthResponse> resp = new Response<>();
        User user;

        try {
            user = userService.createNewUser(request.toUser()).getUser();
        } catch (ExpUserWithThisEmailAlreadyExists | ExpUsernameAlreadyExists e){
            resp.setMessage(e.getMessage());
            resp.setStatus(Response.FAILED);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
        }

        String token = jwtService.generateToken(user);
        resp.setData(new AuthResponse(token, user.getUsername()));
        resp.setStatus(Response.OK);

        return ResponseEntity.status(HttpStatus.CREATED).header(HEADER_NAME, BEARER_PREFIX + token).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        Response<AuthResponse> failed = Response.of(new AuthResponse());
        failed.setMessage("Invalid credentials");
        failed.setStatus(Response.FAILED);

        User user = userService.getUserByEitherUsernameOrUsername(request.getUsername());

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failed);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failed);
        }

        String token = jwtService.generateToken(user);
        Response<AuthResponse> resp = Response.of(new AuthResponse(token, user.getUsername()));
        resp.setMessage("Successfully log on the server, welcome :)");
        resp.setStatus(Response.OK);
        return ResponseEntity.ok().header(HEADER_NAME, BEARER_PREFIX + token).body(resp);
    }

    @PostMapping("/validate/token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token){
        boolean isValid;
        try {
            isValid = jwtService.isTokenValid(token);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return ResponseEntity.ok(isValid);
    }

}