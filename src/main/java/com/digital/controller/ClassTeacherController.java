package com.digital.controller;

import com.digital.dto.AssignTeacherRequest;
import com.digital.dto.AssignedTeacherResponse;
import com.digital.servicei.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/class-teachers") // Base path
@RequiredArgsConstructor // Lombok will inject TeacherService
@CrossOrigin("*")
public class ClassTeacherController {

    private final TeacherService teacherService; // Injected instance

    // Assign Teacher to Class/Section
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/classes/{classId}/sections/{sectionId}/assign-teacher")
    public ResponseEntity<AssignedTeacherResponse> assignTeacher(
            @PathVariable Long classId,
            @PathVariable Long sectionId,
            @RequestBody AssignTeacherRequest request) {

        return ResponseEntity.ok(teacherService.assignTeacher(classId, sectionId, request));
    }

//view assigned teachers
@GetMapping("/class/{classId}/teachers")
public ResponseEntity<List<AssignedTeacherResponse>> getAssignedTeachers(@PathVariable Long classId) {
    return ResponseEntity.ok(teacherService.getAssignedTeachers(classId));
}


}
