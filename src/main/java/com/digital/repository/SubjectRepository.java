package com.digital.repository;

import com.digital.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsBySubjectCode(String subjectCode);

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAllBySchoolClass_ClassId(Long classId);

    List<Subject> findAllByTeacher_Id(Long teacherId);

}
