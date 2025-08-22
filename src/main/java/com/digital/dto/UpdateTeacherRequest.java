package com.digital.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherRequest {
    private String subject;
    private String phone;
    private String email;
    private String Qualification;
}

