package com.digital.serviceimpl;

import com.digital.dto.StudentRequest;
import com.digital.dto.StudentResponse;
import com.digital.dto.DashboardResponse;
import com.digital.entity.Student;
import com.digital.entity.User;
import com.digital.enums.Role;
import com.digital.enums.Status;
import com.digital.repository.StudentRepository;
import com.digital.repository.UserRepository;
import com.digital.servicei.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // <-- injected here



    @Override
    public StudentResponse createStudent(StudentRequest request) {
        try {
            // ✅ Create User for Student
            User user = User.builder()
                    .username(request.getEmail())  // ✅ use email as username
                    .email(request.getEmail())
                    .password(passwordEncoder.encode("Default@123")) // default password
                    .role(Role.STUDENT) // assign student role
                    .build();

            // ✅ Create Student
            Student student = Student.builder()
                    .user(user) // cascade will persist user
                    .rollNumber(request.getRollNumber())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .mobileNumber(request.getMobileNumber())
                    .dateOfBirth(request.getDateOfBirth())
                    .gender(request.getGender())
                    .street(request.getStreet())
                    .city(request.getCity())
                    .state(request.getState())
                    .country(request.getCountry())
                    .pinCode(request.getPinCode())
                    .classId(request.getClassId())
                    .sectionId(request.getSectionId())
                    .build();

            Student saved = studentRepository.save(student);
            return StudentResponse.fromEntity(saved);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create student: " + e.getMessage(), e);
        }
    }

    @Override
    public StudentResponse updateStudent(Long studentId, StudentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setRollNumber(request.getRollNumber());
        student.setClassId(request.getClassId());
        student.setSectionId(request.getSectionId());

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentProfile(Long userId) {
        Student student = studentRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToResponse(student);
    }

    @Override
    public StudentResponse updateStudentProfile(Long userId, StudentRequest request) {
        Student student = studentRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setEmail(request.getEmail());
        student.setMobileNumber(request.getMobileNumber());
        student.setStreet(request.getStreet());
        student.setCity(request.getCity());
        student.setState(request.getState());
        student.setCountry(request.getCountry());
        student.setPinCode(request.getPinCode());

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    public DashboardResponse getDashboard(Long userId) {
        Student student = studentRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return DashboardResponse.builder()
                .profile(mapToResponse(student))
                .attendanceSummary("80% attendance") // placeholder
                .pendingAssignments(List.of("Math Homework", "Science Project"))
                .resultOverview("Overall Grade: B+")
                .recentNotifications(List.of("New assignment uploaded", "Fee due reminder"))
                .build();
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentRegId(student.getStudentRegId())
                .rollNumber(student.getRollNumber())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .className("Class-" + student.getClassId()) // later map properly
                .sectionName("Section-" + student.getSectionId())
                .build();
    }
}
