package com.digital.controller;

import com.digital.dto.PTMRequest;
import com.digital.dto.PTMResponse;

import com.digital.entity.PTM;
import com.digital.entity.Student;
import com.digital.enums.PTMStatus;
import com.digital.repository.PTMRepository;
import com.digital.repository.StudentRepository;
import com.digital.servicei.GoogleMeetService;
import com.digital.servicei.PTMService;
import com.digital.servicei.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ptm")
@RequiredArgsConstructor
public class PTMController {

    private final PTMRepository ptmRepository;
    private final StudentRepository studentRepository;
    private final GoogleMeetService googleMeetService;
    private final PTMService ptmService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/schedule")
    public ResponseEntity<?> schedulePTM(@RequestBody PTMRequest request) {
        try {
            PTMResponse response = ptmService.schedulePTM(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PTMResponse>> getAllPTMs() {
        return ResponseEntity.ok(ptmService.getAllPTMs());
    }
}


