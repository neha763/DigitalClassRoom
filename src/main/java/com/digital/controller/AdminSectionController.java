package com.digital.controller;

import com.digital.dto.SectionRequest;
import com.digital.dto.SectionResponse;
import com.digital.entity.Section;
import com.digital.servicei.SectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/section")
@CrossOrigin(origins = "*") // Allow requests from any domain
@PreAuthorize("hasRole('ADMIN')")
public class AdminSectionController {
    private final SectionService sectionService;

    public AdminSectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

//    @PostMapping("/classes/{classId}/sections")
//    public ResponseEntity<Section> addSection(@PathVariable Long classId,
//                                              @RequestBody SectionRequest request) {
//        Section section = Section.builder()
//                .sectionName(request.getSectionName())
//                .capacity(request.getCapacity())
//                .build();
//        Section saved = sectionService.createSection(classId, section);
//        return ResponseEntity.ok(saved);
//    }

    @PostMapping("/classes/{classId}/sections")
    public ResponseEntity<Section> addSection(@PathVariable Long classId,
                                              @Valid @RequestBody SectionRequest request) {
        Section section = Section.builder()
                .sectionName(request.getSectionName())
                .capacity(request.getCapacity())
                .build();
        Section saved = sectionService.createSection(classId, section);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/sections/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Long id,
                                                 @RequestBody SectionRequest request) {
        Section section = Section.builder()
                .sectionName(request.getSectionName())
                .capacity(request.getCapacity())
                .build();
        Section updated = sectionService.updateSection(id, section);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok("Section deleted successfully");
    }

    @GetMapping("/classes/{classId}/sections")

    public ResponseEntity<List<SectionResponse>> getSectionsByClass(@PathVariable Long classId) {
        List<SectionResponse> response = sectionService.getAllSections().stream()
                .filter(s -> s.getSchoolClass().getClassId().equals(classId))
                .map(s -> SectionResponse.builder()
                        .sectionId(s.getSectionId())
                        .sectionName(s.getSectionName())
                        //.capacity(s.getCapacity())
//                        .classId(s.getSchoolClass().getClassId())
//                        .className(s.getSchoolClass().getClassName())
                     .createdAt(s.getCreatedAt())
                     .updatedAt(s.getUpdatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(response);
    }
}
