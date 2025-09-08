package com.digital.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeacherDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String qualification;
    private Integer experienceYears;
    private String gender;
    private String assignedClasses;
    private String dateOfBirth;
    private Long userId; // for user reference

    private List<Long> assignedClassIds; // list of class IDs
    private List<Long> assignedSectionIds; // list of section IDs
    private List<Long> studentIds; // list of student IDs

    // getters and setters
}
