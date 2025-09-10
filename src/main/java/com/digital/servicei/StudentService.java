package com.digital.servicei;

import com.digital.dto.*;
import com.digital.entity.Student;
import com.digital.entity.User;

import java.util.List;

public interface StudentService {
    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Long studentId, StudentRequest request);
    List<StudentResponse> getAllStudents();
    StudentResponse getStudentProfile(Long userId);
    StudentResponse updateStudentProfile(Long userId, StudentRequest request);
    DashboardResponse getDashboard(Long userId);
    String deleteStudent(Long studentId);
    // Enroll student in a class & section
    StudentCreateResponse enrollStudent(Long studentId, EnrollmentRequest request);

    // Get all students in a class
    List<StudentResponse> getStudentsByClass(Long classId, Long sectionId);
    StudentResponse getStudentClassDetails(Long studentId);
    Student getStudentByUsername(String username);
}
