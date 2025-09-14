package com.digital.servicei;

import com.digital.dto.*;
import com.digital.entity.Student;
import com.digital.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Long studentId, StudentRequest request);
    List<StudentResponse> getAllStudents();
    StudentResponse getStudentProfile(Long userId);
    StudentResponse updateStudentProfile(Long userId, StudentRequest request);
    DashboardResponse getDashboard(Long userId);
    String deleteStudent(Long studentId);

    StudentCreateResponse enrollStudent(Long studentId, EnrollmentRequest request);

    List<StudentResponse> getStudentsByClass(Long classId, Long sectionId);

    //    @Override
    //    public StudentResponse getStudentClassDetails(Long studentId) {
    //        Student student = studentRepository.findById(studentId)
    //                .orElseThrow(() -> new RuntimeException("Student not found"));
    //
    //        return StudentResponse.builder()
    //                .classId(student.getSchoolClass().getClassId())
    //                .className(student.getSchoolClass().getClassName())
    //                .sectionId(student.getSection().getSectionId())
    //                .sectionName(student.getSection().getSectionName())
    //                .enrolledAt(student.getCreatedAt())
    //                .build();
    //    }
    StudentResponse getStudentClassDetails(Long studentId);

    Student getStudentByUsername(String username);

    StudentResponse updateMyProfile(StudentProfileUpdateRequest request);
   // Optional<Student> findByUserUsername(String username);   // ✅ correct

    StudentResponse getMyProfile();

    StudentResponse getMyProfile(String username);


    Optional<Student> findByUserUsername(String username);

    Optional<Student> findByUsername(String username);

}
