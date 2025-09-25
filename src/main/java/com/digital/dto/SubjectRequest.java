package com.digital.dto;

import com.digital.entity.SchoolClass;
import com.digital.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Setter
@Getter
@Builder
public class SubjectRequest {

    @NotBlank(message = "Subject name is required")
    @Size(min = 4, max = 50, message = "Subject name must be between 4 to 100 characters")
    private String subjectName; // → The name of the subject (e.g., Mathematics, Science, History)

    private Teacher teacher; // Teacher_Reg_Id → Foreign Key (links the subject to a teacher who teaches it)

    private SchoolClass schoolClass; // → The class/grade in which the subject is taught (e.g., Class 6, Class 10)

    private Year year;
}
