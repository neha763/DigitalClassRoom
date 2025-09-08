package com.digital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClassTeacher {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long classId;
        private Long sectionId;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "teacher_id")
        private Teacher teacher;



        private LocalDateTime assignedAt;

        @PrePersist
        public void onAssign() {
            this.assignedAt = LocalDateTime.now();
        }
    }


