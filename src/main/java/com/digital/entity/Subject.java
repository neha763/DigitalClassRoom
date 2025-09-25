package com.digital.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@JsonIgnoreProperties({"teacher", "schoolClass"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectId;

    @Column(nullable = false)
    private String subjectName; // → The name of the subject (e.g., Mathematics, Science, History)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId", nullable = false)
    private Teacher teacher; // Teacher_Reg_Id → Foreign Key (links the subject to a teacher who teaches it)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private SchoolClass schoolClass; // → The class/grade in which the subject is taught (e.g., Class 6, Class 10)

    @Column(nullable = false)
    private Year year; // The academic year in which the subject is offered
}
