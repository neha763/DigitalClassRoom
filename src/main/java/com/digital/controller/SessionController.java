package com.digital.controller;

import com.digital.dto.SessionRequest;
import com.digital.entity.Session;
import com.digital.servicei.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * This api allows TEACHER to create sessions for students
     * */

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> createSession(@Valid @RequestBody SessionRequest sessionRequest){
        return new ResponseEntity<>(sessionService.createSession(sessionRequest), HttpStatus.CREATED);
    }

    /**
     * This api allows TEACHER, STUDENT, ADMIN to fetch session
     * */

    @PreAuthorize("hasAnyRole('Teacher', 'STUDENT', 'ADMIN')")
    @GetMapping(path = "/get", produces = "application/json")
    public ResponseEntity<List<Session>> getAllSessions(){
        return new ResponseEntity<>(sessionService.getAllSessions(), HttpStatus.OK);
    }
}
