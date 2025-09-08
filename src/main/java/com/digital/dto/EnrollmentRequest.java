package com.digital.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {
    private Long classId;
    private Long sectionId;
}
 