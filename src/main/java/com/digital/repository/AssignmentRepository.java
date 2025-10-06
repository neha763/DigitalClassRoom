package com.digital.repository;

import com.digital.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTeacherId(Long teacherId);
    Optional<Assignment> findByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);

}
