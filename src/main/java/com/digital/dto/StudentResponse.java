package com.digital.dto;

import com.digital.entity.Student;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long studentRegId;
    private String rollNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String gender;

    // Address
    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    // Class & Section
    private Long classId;
    private Long sectionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // username from User entity (optional but useful)
    private String username;

    // Factory method to map from entity
    public static StudentResponse fromEntity(Student student) {
        return StudentResponse.builder()
                .studentRegId(student.getStudentRegId())
                .rollNumber(student.getRollNumber())
                .firstName(student.getFirstName())
                .middleName(student.getMiddleName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .street(student.getStreet())
                .city(student.getCity())
                .state(student.getState())
                .country(student.getCountry())
                .pinCode(student.getPinCode())
                .classId(student.getClassId())
                .sectionId(student.getSectionId())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .username(student.getUser() != null ? student.getUser().getUsername() : null)
                .build();
    }
}
