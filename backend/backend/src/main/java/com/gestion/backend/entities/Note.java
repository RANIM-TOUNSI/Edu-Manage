package com.gestion.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "note")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valeur", nullable = false)
    private Double valeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_matricule", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_code", nullable = false)
    private Cours cours;
}
