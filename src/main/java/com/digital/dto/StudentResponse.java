package com.digital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {
    private Long studentRegId;
    private String rollNumber;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String className;
    private String sectionName;
}
