package com.digital.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherRequest {
    private String firstName;
    private  String LastName;
    private String fullName;
    private String subject;
    private String phone;
    private String email;
    private  String Qualification;



}
