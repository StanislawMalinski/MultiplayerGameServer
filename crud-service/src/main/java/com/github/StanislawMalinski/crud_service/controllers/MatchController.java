package com.github.stanislawmalinski.crud_service.controllers;

import com.github.stanislawmalinski.crud_service.models.Match;
import com.github.stanislawmalinski.crud_service.models.MatchPlayout;
import com.github.stanislawmalinski.crud_service.response_request.ExpMatchDoesNotExists;
import com.github.stanislawmalinski.crud_service.response_request.ExpUserDoesNotExists;
import com.github.stanislawmalinski.crud_service.response_request.MatchDTO;
import com.github.stanislawmalinski.crud_service.response_request.Response;
import com.github.stanislawmalinski.crud_service.services.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@AllArgsConstructor
public class MatchController {
    MatchService service;
    @GetMapping("/{id}")
    public ResponseEntity<Response<Match>> getMatchById(@PathVariable Long id){
        Match m;
        Response<Match> resp = new Response<>();
        try {
            m = service.getMatchById(id);
        } catch (ExpMatchDoesNotExists e){
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        resp.setData(m);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<Page<Match>>> getMatchesForUser(@PathVariable Long userId) {
        return getMatchesForUser(userId, 0);
    }

    @GetMapping("/user/{userId}/{page}")
    public ResponseEntity<Response<Page<Match>>> getMatchesForUser(@PathVariable Long userId, @PathVariable int page){
        Response<Page<Match>> resp = new Response<>();
        Page<Match> matches;
        try {
            matches = service.getMatchesForUser(userId, page);
        } catch (ExpUserDoesNotExists e) {
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        return ResponseEntity.ok(Response.of(matches));
    }

    @PostMapping("/persist")
    public ResponseEntity<Response<MatchDTO>> persistPlayedMatch(@RequestBody MatchDTO match) {
        MatchDTO m;
        try {
            m = service.persistMatch(match);
        }catch (Exception e){
            Response<MatchDTO> resp = new Response<>();
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.of(m));
    }

    @PostMapping("/persist/playout")
    public ResponseEntity<Response<Long>> persistPlayedMatchPlayout(@RequestBody MatchPlayout matchPlayout){
        Response<Long> resp = new Response<>();
        Long id;
        try {
            id = service.persistMatchPlayout(matchPlayout);
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
        resp.setData(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
