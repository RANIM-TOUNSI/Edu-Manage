package com.gestion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "student_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<Student> students;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<Seance> seances;
}
