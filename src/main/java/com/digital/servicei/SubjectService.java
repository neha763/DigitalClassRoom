

package com.digital.servicei;
import com.digital.dto.SubjectRequest;
import com.digital.dto.SubjectResponse;

package com.digital.servicei;

import com.digital.dto.SubjectRequest;
import com.digital.entity.Subject;


import java.util.List;

public interface SubjectService {

    SubjectResponse createSubject(SubjectRequest request);
    SubjectResponse updateSubject(Long subjectId, SubjectRequest request);
    void deleteSubject(Long subjectId);
    List<SubjectResponse> getAllSubjects();
    SubjectResponse getSubjectById(Long subjectId);


    String addSubject(SubjectRequest subjectRequest);

    Subject getSubject(Long subjectId);

    List<Subject> getSubjectsByClassId(Long classId);

    List<Subject> getSubjectsByTeacherId(Long teacherId);

    String updateSubject(Long subjectId, Long teacherId);

}
