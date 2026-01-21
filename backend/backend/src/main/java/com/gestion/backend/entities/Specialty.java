package com.gestion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "specialties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "specialty")
    @JsonIgnore
    private List<StudentGroup> groups;

    @OneToMany(mappedBy = "specialty")
    @JsonIgnore
    private List<Course> courses;
}
