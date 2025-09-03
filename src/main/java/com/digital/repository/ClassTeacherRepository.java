package com.digital.repository;

import com.digital.entity.ClassTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassTeacherRepository extends JpaRepository<ClassTeacher, Long> {
    boolean existsByClassIdAndSectionIdAndTeacherId(Long classId, Long sectionId, Long teacherId);

    List<ClassTeacher> findByClassId(Long classId);
    List<ClassTeacher> findByTeacherId(Long teacherId);

}
