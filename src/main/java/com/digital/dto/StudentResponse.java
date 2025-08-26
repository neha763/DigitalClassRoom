package com.digital.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long studentRegId;
    private Long userId;
    private String rollNumber;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String className;
    private String sectionName;

    public static StudentResponse fromEntity(com.digital.entity.Student student) {
        return StudentResponse.builder()
                .studentRegId(student.getStudentRegId())
                .userId(student.getUser().getUserId())
                .rollNumber(student.getRollNumber())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .className("Class-" + student.getClassId())     // TODO: replace with Class entity name
                .sectionName("Section-" + student.getSectionId()) // TODO: replace with Section entity name
                .build();
    }
}
