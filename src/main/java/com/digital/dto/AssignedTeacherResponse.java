package com.digital.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Data
@Builder
public class AssignedTeacherResponse {
    private Long id;
    private Long classId;
    private String className;
    private Long sectionId;
    private String sectionName;
    private Long teacherId;
    private String teacherName;
    private LocalDateTime assignedAt;
}
