package com.digital.dto;

import com.digital.enums.AttendanceStatus;
import com.digital.enums.MarkBy;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@Builder
public class ViewStudentCheckListResponse {

    private Long studentId;

    private String rollNo;

    private String fullName;

    private LocalTime joinTime;

    private LocalTime exitTime;

    private Long durationMinutes;

    private Long sessionId;

    private String sessionTopic;

    private Long teacherId;

    private String teacherName;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @Enumerated(EnumType.STRING)
    private MarkBy markBy;
}
