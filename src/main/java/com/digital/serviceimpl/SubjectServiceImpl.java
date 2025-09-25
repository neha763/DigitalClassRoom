package com.digital.serviceimpl;

import com.digital.dto.SubjectRequest;
import com.digital.entity.SchoolClass;
import com.digital.entity.Subject;
import com.digital.entity.Teacher;
import com.digital.exception.ResourceNotFoundException;
import com.digital.repository.ClassRepository;
import com.digital.repository.SubjectRepository;
import com.digital.repository.TeacherRepository;
import com.digital.servicei.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    @Override
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
