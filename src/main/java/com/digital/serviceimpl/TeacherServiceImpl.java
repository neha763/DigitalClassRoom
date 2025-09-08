package com.digital.serviceimpl;


import com.digital.dto.TeacherCreateRequest;
import com.digital.dto.TeacherDto;
import com.digital.entity.*;
import com.digital.repository.*;
import com.digital.servicei.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    //    private final ClassTeacherRepository classTeacherRepository;
    private final ClassRepository classRepository;
    private final SectionRepository sectionRepository;
    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private TeacherDto mapToDto(Teacher teacher) {
        return TeacherDto.builder()
                .userId(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .qualification(teacher.getQualification())
                .experienceYears(teacher.getExperienceYears())
                .gender(teacher.getGender())
                //.assignedClasses(teacher.getAssignedClasses())
                .dateOfBirth(teacher.getDateOfBirth())

                .build();
    }

//    @Override
//    public TeacherDto createTeacher(Teacher teacher)
//    {
//        return mapToDto(teacherRepository.save(teacher));
//    }

//    @Override
//    public TeacherDto createTeacher(CreateTeacherRequest.TeacherCreateRequest teacher) {
//        return null;
//    }



    @Override
    public TeacherDto getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    @Override
    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDto updateTeacher(Long id, Teacher teacher) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existing.setFirstName(teacher.getFirstName());
        existing.setLastName(teacher.getLastName());
        existing.setEmail(teacher.getEmail());
        existing.setPhone(teacher.getPhone());
        existing.setQualification(teacher.getQualification());
        existing.setExperienceYears(teacher.getExperienceYears());
        existing.setGender(teacher.getGender());
      //  existing.setAssignedClasses(teacher.getAssignedClasses());
        existing.setDateOfBirth(teacher.getDateOfBirth());

        return mapToDto(teacherRepository.save(existing));
    }

    @Override
    public String deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
        return "Teacher with ID " + id + " has been successfully deleted.";
    }


//    @Override
//    public AssignedTeacherResponse assignTeacher(Long classId, Long sectionId, AssignTeacherRequest request) {
//        // check class, section, teacher exist
//        SchoolClass schoolClass = classRepository.findById(classId)
//                .orElseThrow(() -> new RuntimeException("Class not found"));
//
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//
//        Teacher teacher = teacherRepository.findById(request.getTeacherId())
//                .orElseThrow(() -> new RuntimeException("Teacher not found"));
//
//        // prevent duplicate mapping
//        if (classTeacherRepository.existsByClassIdAndSectionIdAndTeacherId(classId, sectionId, teacher.getId())) {
//            throw new RuntimeException("Teacher already assigned to this class and section");
//        }
//
//        ClassTeacher mapping = ClassTeacher.builder()
//                .classId(classId)
//                .sectionId(sectionId)
//                .teacher(teacher) // set Teacher entity
//                .build();
//
//        classTeacherRepository.save(mapping); // JPA will persist teacher_id foreign key automatically
//
//        return AssignedTeacherResponse.builder()
//                .id(mapping.getId())
//                .classId(classId)
//                .className(schoolClass.getClassName())
//                .sectionId(sectionId)
//                .sectionName(section.getSectionName())
//                .teacherId(mapping.getTeacher().getId())
//                .teacherName(mapping.getTeacher().getFirstName() + " " + mapping.getTeacher().getLastName())
//                .assignedAt(mapping.getAssignedAt())
//                .build();
//
//    }
//
//    @Override
//    public List<AssignedTeacherResponse> getAssignedTeachers(Long classId) {
//        SchoolClass schoolClass = classRepository.findById(classId)
//                .orElseThrow(() -> new RuntimeException("Class not found"));
//
//        return classTeacherRepository.findByClassId(classId).stream().map(mapping -> {
//            Section section = sectionRepository.findById(mapping.getSectionId()).orElse(null);
//            Teacher teacher = mapping.getTeacher(); // use the entity
//
//            return AssignedTeacherResponse.builder()
//                    .id(mapping.getId())
//                    .classId(classId)
//                    .className(schoolClass.getClassName())
//                    .sectionId(mapping.getSectionId())
//                    .sectionName(section != null ? section.getSectionName() : null)
//                    .teacherId(teacher != null ? teacher.getId() : null)   // ✅ fix here
//                    .teacherName(teacher != null ? teacher.getFirstName() + " " + teacher.getLastName() : null) // ✅ fix here
//                    .assignedAt(mapping.getAssignedAt())
//                    .build();
//        }).collect(Collectors.toList());
//    }
//

    /// /teacher view
//    @Override
//    public List<AssignedTeacherResponse> getAssignedClassesForTeacher(Long teacherId) {
//        List<ClassTeacher> assignments = classTeacherRepository.findByTeacherId(teacherId);
//
//        return assignments.stream().map(mapping -> {
//            SchoolClass schoolClass = classRepository.findById(mapping.getClassId()).orElse(null);
//            Section section = sectionRepository.findById(mapping.getSectionId()).orElse(null);
//            Teacher teacher = mapping.getTeacher(); // fetch teacher entity
//
//            return AssignedTeacherResponse.builder()
//                    .id(mapping.getId())
//                    .classId(mapping.getClassId())
//                    .className(schoolClass != null ? schoolClass.getClassName() : null)
//                    .sectionId(mapping.getSectionId())
//                    .sectionName(section != null ? section.getSectionName() : null)
//                    .teacherId(teacher != null ? teacher.getId() : null) // set teacherId
//                    .teacherName(teacher != null ? teacher.getFirstName() + " " + teacher.getLastName() : null) // set teacherName
//                    .assignedAt(mapping.getAssignedAt())
//                    .build();
//        }).collect(Collectors.toList());
//    }
//    @Override
//    public Teacher getTeacherByUsername(String username) {
//        return teacherRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Teacher not found with username: " + username));
//    }
    @Override
    public Teacher createTeacher(TeacherCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SchoolClass> schoolClasses = classRepository.findAllById(request.getAssignedClassIds());
        List<Section> sections = sectionRepository.findAllById(request.getAssignedSectionIds());
        List<Student> students = studentRepository.findAllById(request.getStudentIds());

        Teacher teacher = Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .user(user)
                .assignedClass(schoolClasses)
                .assignedSection(sections)
                .student(students)
                .build();

        return teacherRepository.save(teacher);
    }

}


