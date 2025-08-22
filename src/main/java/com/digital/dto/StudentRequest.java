package com.digital.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    private String rollNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private Long classId;
    private Long sectionId;
}
