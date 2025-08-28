package com.digital.servicei;

import com.digital.dto.TeacherDto;
import com.digital.entity.Teacher;

import java.util.List;

public interface TeacherService {
    TeacherDto createTeacher(Teacher teacher);
    TeacherDto getTeacherById(Long id);
    List<TeacherDto> getAllTeachers();
    TeacherDto updateTeacher(Long id, Teacher teacher);
    void deleteTeacher(Long id);
}
