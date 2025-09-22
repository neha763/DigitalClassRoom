package com.digital.repository;

import com.digital.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    Optional<FeeStructure> findByClassIdAndAcademicYear(Long classId, String academicYear);
}
