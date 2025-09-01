package com.digital.servicei;

import com.digital.dto.AssignTeacherRequest;
import com.digital.dto.AssignedTeacherResponse;
import com.digital.dto.TeacherDto;
import com.digital.entity.Teacher;

import java.util.List;

public interface TeacherService {
    TeacherDto createTeacher(Teacher teacher);
    TeacherDto getTeacherById(Long id);
    List<TeacherDto> getAllTeachers();
    TeacherDto updateTeacher(Long id, Teacher teacher);
    void deleteTeacher(Long id);
    AssignedTeacherResponse assignTeacher(Long classId, Long sectionId, AssignTeacherRequest request);
    List<AssignedTeacherResponse> getAssignedTeachers(Long classId);
    List<AssignedTeacherResponse> getAssignedClassesForTeacher(Long teacherId);
    Teacher getTeacherByUsername(String username);

}
