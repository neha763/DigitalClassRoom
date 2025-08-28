package com.digital.serviceimpl;

import com.digital.dto.TeacherDto;
import com.digital.entity.Teacher;
import com.digital.repository.TeacherRepository;
import com.digital.servicei.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private TeacherDto mapToDto(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .qualification(teacher.getQualification())
                .experienceYears(teacher.getExperienceYears())
                .gender(teacher.getGender())
                .assignedClasses(teacher.getAssignedClasses())
                .dateOfBirth(teacher.getDateOfBirth())
                .build();
    }

    @Override
    public TeacherDto createTeacher(Teacher teacher) {
        return mapToDto(teacherRepository.save(teacher));
    }

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
        existing.setAssignedClasses(teacher.getAssignedClasses());
        existing.setDateOfBirth(teacher.getDateOfBirth());

        return mapToDto(teacherRepository.save(existing));
    }

    @Override
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}
