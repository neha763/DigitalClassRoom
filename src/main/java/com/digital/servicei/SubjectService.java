package com.digital.servicei;

import com.digital.dto.SubjectRequest;
import com.digital.entity.Subject;

import java.util.List;

public interface SubjectService {

    String addSubject(SubjectRequest subjectRequest);

    Subject getSubject(Long subjectId);

    List<Subject> getSubjectsByClassId(Long classId);

    List<Subject> getSubjectsByTeacherId(Long teacherId);

    String updateSubject(Long subjectId, Long teacherId);
}
