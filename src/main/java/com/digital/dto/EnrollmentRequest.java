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
 
=======
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentRequest {
    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Section ID is required")
    private Long sectionId;
}

