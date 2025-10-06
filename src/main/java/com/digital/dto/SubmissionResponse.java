package com.digital.dto;

import com.digital.enums.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmissionResponse {
    private Long submissionId;
    private Long assignmentId;
    private byte[] fileUrl;
    private Long examId;
    private Long studentId;
    private String answers;             // JSON string of answers
    private BigDecimal obtainedMarks;   // BigDecimal to match entity
    private BigDecimal percentage;
    private Double marks;
    private String feedback;
    private String grade;               // String
    private SubmissionStatus status;             // Enum name
//    private Long evaluatedBy;
//    private LocalDateTime evaluatedAt;
    private LocalDateTime submittedAt;
    private List<SubjectResultResponse> subjects;
    private List<ReportCardSubjectDto> subjectMarks;
}
