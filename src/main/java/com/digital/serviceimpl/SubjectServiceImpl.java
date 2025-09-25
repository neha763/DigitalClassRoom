package com.digital.serviceimpl;

import com.digital.dto.SubjectRequest;

import com.digital.dto.SubjectResponse;
import com.digital.entity.SchoolClass;
import com.digital.entity.Subject;
import com.digital.entity.Teacher;

import com.digital.entity.SchoolClass;
import com.digital.entity.Subject;
import com.digital.entity.Teacher;
import com.digital.exception.ResourceNotFoundException;

import com.digital.repository.ClassRepository;
import com.digital.repository.SubjectRepository;
import com.digital.repository.TeacherRepository;
import com.digital.servicei.SubjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;


    public SubjectResponse createSubject(SubjectRequest request) {
        if (subjectRepository.existsBySubjectCode(request.getSubjectCode())) {
            throw new RuntimeException("Subject code already exists");
        }

        // Fetch related entities by IDs
        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Teacher teacher = null;
        if (request.getTeacherId() != null) {
            teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        Subject subject = Subject.builder()
                .subjectName(request.getSubjectName())
                .subjectCode(request.getSubjectCode())
                .schoolClass(schoolClass)
                .teacher(teacher)
                .description(request.getDescription())
                .maxMarks(request.getMaxMarks())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Subject saved = subjectRepository.save(subject);
        return mapToResponse(saved);
    }

    @Override
    public SubjectResponse updateSubject(Long subjectId, SubjectRequest request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (request.getSubjectName() != null)
            subject.setSubjectName(request.getSubjectName());

        if (request.getClassId() != null) {
            SchoolClass schoolClass = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            subject.setSchoolClass(schoolClass);
        }

        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            subject.setTeacher(teacher);
        }

        if (request.getDescription() != null)
            subject.setDescription(request.getDescription());

        if (request.getIsActive() != null)
            subject.setIsActive(request.getIsActive());

        Subject updated = subjectRepository.save(subject);
        return mapToResponse(updated);
    }

    @Override
    public void deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponse getSubjectById(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return mapToResponse(subject);
    }

    private SubjectResponse mapToResponse(Subject subject) {
        return SubjectResponse.builder()
                .subjectId(subject.getSubjectId())
                .subjectName(subject.getSubjectName())
                .subjectCode(subject.getSubjectCode())
                .classId(subject.getSchoolClass() != null ? subject.getSchoolClass().getClassId() : null)
                .className(subject.getSchoolClass() != null ? subject.getSchoolClass().getClassName() : null)
                .teacherId(subject.getTeacher() != null ? subject.getTeacher().getId() : null)
                .teacherName(subject.getTeacher() != null ? subject.getTeacher().getFirstName() : null)
                .description(subject.getDescription())
                .isActive(subject.getIsActive())
                .build();
    }


    public String addSubject(SubjectRequest subjectRequest) {

        SchoolClass schoolClass = classRepository.findById(subjectRequest.getSchoolClass().getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("School class with id: " +
                        subjectRequest.getSchoolClass().getClassId() + " not present in database"));

        Teacher teacher = teacherRepository.findById(subjectRequest.getTeacher().getId()).orElseThrow(() ->
                new ResourceNotFoundException("Teacher with id: " + subjectRequest.getTeacher().getId()
                        + " not present in database"));

        Subject subject = Subject.builder()
                .subjectName(subjectRequest.getSubjectName())
                .teacher(teacher)
                .schoolClass(schoolClass)
                .year(subjectRequest.getYear())
                .build();

        subjectRepository.save(subject);

        return "Subject has added successfully.";
    }

    @Override
    public Subject getSubject(Long subjectId) {

        return subjectRepository.findById(subjectId).orElseThrow(() ->
                new ResourceNotFoundException("Subject with id: " + subjectId + " not present in database"));
    }

    @Override
    public List<Subject> getSubjectsByClassId(Long classId) {
        return subjectRepository.findAllBySchoolClass_ClassId(classId);
    }

    @Override
    public List<Subject> getSubjectsByTeacherId(Long teacherId) {
        return subjectRepository.findAllByTeacher_Id(teacherId);
    }

    @Override
    public String updateSubject(Long subjectId, Long teacherId) {

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() ->
                new ResourceNotFoundException("Can't update the given subject as subject with id: "
                        + subjectId + " not present in database"));

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() ->
                    new ResourceNotFoundException("Teacher with id: " + teacherId + " not present in database"));

        subject.setTeacher(teacher);
        subjectRepository.save(subject);

        return "Subject with id: " + subjectId + " has updated successfully.";
    }

}
