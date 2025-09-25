package com.digital.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

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


    @NotBlank
    private String subjectName;

    @NotBlank
    @Column(unique = true)
    private String subjectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean isActive = true;
    @Column(nullable = false)
    private BigDecimal maxMarks;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

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
