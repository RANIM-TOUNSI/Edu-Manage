package com.gestion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudentGroup group;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Grade> grades;

    @PrePersist
    public void generateMatricule() {
        if (this.matricule == null || this.matricule.isEmpty()) {
            this.matricule = "STU-" + System.currentTimeMillis();
        }
        if (this.registrationDate == null) {
            this.registrationDate = LocalDate.now();
        }
    }
}
