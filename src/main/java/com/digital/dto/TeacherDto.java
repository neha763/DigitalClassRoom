package com.digital.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
    private String firstName;
    private String lastName;
    private Long id;
    private String fullName;
    private String subject;
    private String phone;
    private String email;
    private String Qualification;
}
