package com.digital.repository;

import com.digital.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserUserId(Long userId);
    Optional<Student> findByRollNumber(String rollNumber);
    boolean existsByEmail(String email);

    // Check if roll number already exists in a given class
    boolean existsByRollNumberAndClassId(String rollNumber, Long classId);

    Optional<Student> findByUser_Username(String username);

    List<Student> findAllByClassIdAndSectionId(Long classId, Long sectionId);
}
