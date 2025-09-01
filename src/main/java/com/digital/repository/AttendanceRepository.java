package com.digital.repository;

import com.digital.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudent_StudentRegIdAndSession_SessionId(Long studentRegId, Long sessionId);

    Boolean existsByStudent_StudentRegIdAndSession_SessionId(Long studentRegId, Long sessionId);

    List<Attendance> findAllBySession_SessionId(Long sessionId);

    List<Attendance> findAllByDateBetween(LocalDate fromDate, LocalDate toDate);
}
