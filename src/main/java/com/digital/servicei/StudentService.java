package com.digital.servicei;

import com.digital.dto.*;
import com.digital.entity.Student;
import com.digital.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    // Create Student
    StudentResponse createStudent(StudentRequest request);
    StudentResponse createStudent(StudentRequest request, User user);

    // Update Student
    StudentResponse updateStudent(Long studentId, StudentRequest request);

    // Get Students
    List<StudentResponse> getAllStudents();
    StudentResponse getStudentProfile(Long userId);
    List<StudentResponse> getStudentsByClass(Long classId, Long sectionId);
    StudentResponse getStudentClassDetails(Long studentId);


    // Update Student Profile
    StudentResponse updateStudentProfile(Long userId, StudentRequest request);

    Student getStudentByUsername(String username);

    Student getStudentById(Long studentId);



    StudentResponse updateMyProfile(StudentProfileUpdateRequest request);

    // Current logged-in Student
    StudentResponse getMyProfile();
    StudentResponse getMyProfile(String username);

    // Dashboard
    DashboardResponse getDashboard(Long userId);

    // Enroll Student in Class/Section
    StudentCreateResponse enrollStudent(Long studentId, EnrollmentRequest request);

    // Delete Student
    String deleteStudent(Long studentId);

    // Fetch Student entity directly
    Student getStudentByUsername(String username);
    Student getStudentById(Long studentId);

    // Optional fetch methods
    Optional<Student> findByUserUsername(String username);
    Optional<Student> findByUsername(String username);

}
