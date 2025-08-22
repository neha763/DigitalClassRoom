package com.digital.servicei;


import com.digital.dto.CreateTeacherRequest;
import com.digital.dto.TeacherDto;
import com.digital.dto.UpdateTeacherRequest;

import java.util.List;

public interface TeacherService {
    TeacherDto createTeacher(CreateTeacherRequest request);
    void deleteTeacher(Long id);
    List<TeacherDto> getAllTeachers();
    TeacherDto getMyProfile();
    TeacherDto updateMyProfile(UpdateTeacherRequest request);
}

