package com.digital.entity;

import com.digital.enums.PTMStatus;
import com.digital.enums.PTMType;
import jakarta.persistence.*;
import lombok.*;
import com.digital.entity.Student; // add this


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ptm")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PTM {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ptmId;

    private String title;
    private String description;
    private LocalDateTime meetingDateTime;
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private PTMType type;

    private String joinLink;
    private String venue;

    @Enumerated(EnumType.STRING)
    private PTMStatus status;

    @ManyToMany
    @JoinTable(
            name = "ptm_students",
            joinColumns = @JoinColumn(name = "ptm_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
    public List<Long> getStudentIds() {
        if (students == null) return List.of();
        return students.stream()
                .map(Student::getStudentRegId) // use getStudentId() if your field name is studentId
                .toList();
    }

}
