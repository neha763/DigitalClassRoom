package com.digital.repository;

import com.digital.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByEmail(String email);

    Optional<Teacher> findByEmail(String email); // <-- Add this

    Optional<Teacher> findByUser_Username(String username);
}
