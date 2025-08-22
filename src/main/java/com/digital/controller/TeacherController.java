package com.digital.controller;

import com.digital.dto.CreateTeacherRequest;
import com.digital.dto.TeacherDto;
import com.digital.dto.UpdateTeacherRequest;
import com.digital.servicei.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // usually only admin can create teachers
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody CreateTeacherRequest request) {
        return ResponseEntity.ok(teacherService.createTeacher(request));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/me")
    public ResponseEntity<TeacherDto> getMyProfile() {
        return ResponseEntity.ok(teacherService.getMyProfile());
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/me")
    public ResponseEntity<TeacherDto> updateMyProfile(@RequestBody UpdateTeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateMyProfile(request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }
}
