package com.digital.serviceimpl;
import com.digital.dto.*;
import com.digital.entity.SchoolClass;
import com.digital.entity.Section;
import com.digital.entity.Student;
import com.digital.entity.User;
import com.digital.exception.StudentNotFoundException;
import com.digital.repository.ClassRepository;
import com.digital.repository.SectionRepository;
import com.digital.repository.StudentRepository;
import com.digital.repository.UserRepository;
import com.digital.servicei.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;
    private final ClassRepository classRepository;
    private final SectionRepository sectionRepository;

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + request.getClassId()));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with ID: " + request.getSectionId()));

        Student student = Student.builder()
                .user(user)   // 🔑 link User <-> Student
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
                .schoolClass(schoolClass)
                .section(section)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return StudentResponse.fromEntity(studentRepository.save(student));
    }


    @Override
    public StudentResponse updateStudent(Long studentId, StudentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setRollNumber(request.getRollNumber());
        student.setFirstName(request.getFirstName());
        student.setMiddleName(request.getMiddleName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setMobileNumber(request.getMobileNumber());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setStreet(request.getStreet());
        student.setCity(request.getCity());
        student.setState(request.getState());
        student.setCountry(request.getCountry());
        student.setPinCode(request.getPinCode());

        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + request.getClassId()));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with ID: " + request.getSectionId()));

        student.setSchoolClass(schoolClass);
        student.setSection(section);

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

    @Override
    public String deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        User user = student.getUser();
        studentRepository.delete(student);
        if (user != null) {
            userRepository.delete(user);
        }

        return "Student deleted successfully with id: " + studentId;
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentRegId(student.getStudentRegId())
                .rollNumber(student.getRollNumber())
                .firstName(Optional.ofNullable(student.getFirstName()).orElse(""))
                .middleName(Optional.ofNullable(student.getMiddleName()).orElse(""))
                .lastName(Optional.ofNullable(student.getLastName()).orElse(""))
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .street(student.getStreet())
                .city(student.getCity())
                .state(student.getState())
                .country(student.getCountry())
                .pinCode(student.getPinCode())
                .classId(student.getSchoolClass() != null ? student.getSchoolClass().getClassId() : null)
                .className(student.getSchoolClass() != null ? student.getSchoolClass().getClassName() : null)
                .sectionId(student.getSection() != null ? student.getSection().getSectionId() : null)
                .sectionName(student.getSection() != null ? student.getSection().getSectionName() : null)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .username(student.getUser() != null ? student.getUser().getUsername() : null)
                .build();
    }


    @Override
    public StudentCreateResponse enrollStudent(Long studentId, EnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new EntityNotFoundException("Class not found with ID: " + request.getClassId()));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found with ID: " + request.getSectionId()));

        student.setSchoolClass(schoolClass);
        student.setSection(section);

        if (student.getEnrolledAt() == null) {
            student.setEnrolledAt(LocalDateTime.now());
        }
        studentRepository.save(student);
        return StudentCreateResponse.fromEntity(student);
    }

    @Override
    public List<StudentResponse> getStudentsByClass(Long classId, Long sectionId) {
        if (sectionId != null) {
            return studentRepository.findBySchoolClass_ClassIdAndSection_SectionId(classId, sectionId)
                    .stream().map(this::toResponse).collect(Collectors.toList());
        } else {
            return studentRepository.findBySchoolClass_ClassId(classId)
                    .stream().map(this::toResponse).collect(Collectors.toList());
        }
    }

    private StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .studentRegId(student.getStudentRegId())
                .rollNumber(student.getRollNumber())
                .firstName(Optional.ofNullable(student.getFirstName()).orElse(""))
                .middleName(Optional.ofNullable(student.getMiddleName()).orElse(""))
                .lastName(Optional.ofNullable(student.getLastName()).orElse(""))
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .street(student.getStreet())
                .city(student.getCity())
                .state(student.getState())
                .country(student.getCountry())
                .pinCode(student.getPinCode())
                .classId(student.getSchoolClass() != null ? student.getSchoolClass().getClassId() : null)
                .className(student.getSchoolClass() != null ? student.getSchoolClass().getClassName() : null)
                .sectionId(student.getSection() != null ? student.getSection().getSectionId() : null)
                .sectionName(student.getSection() != null ? student.getSection().getSectionName() : null)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .username(student.getUser() != null ? student.getUser().getUsername() : null)
                .build();
    }


@Override
public StudentResponse getStudentClassDetails(Long studentId) {
    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    return StudentResponse.builder()
            .studentRegId(student.getStudentRegId())
            .rollNumber(student.getRollNumber())
            .firstName(student.getFirstName())
            .middleName(student.getMiddleName())
            .lastName(student.getLastName())
            .email(student.getEmail())
            .mobileNumber(student.getMobileNumber())
            .dateOfBirth(student.getDateOfBirth())
            .gender(student.getGender())
            .street(student.getStreet())
            .city(student.getCity())
            .state(student.getState())
            .country(student.getCountry())
            .pinCode(student.getPinCode())
            .classId(student.getSchoolClass() != null ? student.getSchoolClass().getClassId() : null)
            .className(student.getSchoolClass() != null ? student.getSchoolClass().getClassName() : null)
            .sectionId(student.getSection() != null ? student.getSection().getSectionId() : null)
            .sectionName(student.getSection() != null ? student.getSection().getSectionName() : null)
            .enrolledAt(student.getCreatedAt())
            .createdAt(student.getCreatedAt())
            .updatedAt(student.getUpdatedAt())
            .username(student.getUser() != null ? student.getUser().getUsername() : null)
            .build();
}
    @Override
    public Student getStudentByUsername(String username) {
        // Fetch User from User table
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Fetch Student linked to this User
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found for user: " + username));
    }
    @Override
    public StudentResponse getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getMyProfile(username);
    }

    @Override
    public StudentResponse getMyProfile(String username) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found for username: " + username
                ));
        return toResponse(student);
    }

    @Override
    public StudentResponse updateMyProfile(StudentProfileUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found for username: " + username
                ));

        student.setEmail(request.getEmail());
        student.setMobileNumber(request.getMobileNumber());
        student.setStreet(request.getStreet());
        student.setCity(request.getCity());
        student.setState(request.getState());
        student.setCountry(request.getCountry());
        student.setPinCode(request.getPinCode());
        student.setUpdatedAt(LocalDateTime.now());

        return toResponse(studentRepository.save(student));
    }
    public Student getCurrentStudent(String username) {
        return studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new StudentNotFoundException(
                        "No student profile linked with username: " + username
                ));
    }
    @Override
    public Optional<Student> findByUserUsername(String username) {
        return studentRepository.findByUserUsername(username);
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(studentRepository::findByUser);
    }


}
