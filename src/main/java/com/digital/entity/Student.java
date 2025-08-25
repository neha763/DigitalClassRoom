package com.digital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentRegId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    @Column(nullable = false, unique = true)
    @NotBlank(message = "Roll number is required")
    @Pattern(regexp = "^[A-Z0-9]{5,15}$", message = "Roll number must be alphanumeric (5–15 characters)")
    private String rollNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobileNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    // Address
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    @Pattern(regexp = "^[0-9]{5,10}$", message = "Pin code must be 5–10 digits")
    private String pinCode;

    // Class & Section
    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Section ID is required")
    private Long sectionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
