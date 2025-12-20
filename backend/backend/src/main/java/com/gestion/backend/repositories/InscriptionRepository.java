package com.gestion.backend.repositories;

import com.gestion.backend.entities.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    boolean existsByEtudiantMatriculeAndCoursCode(String etudiantMatricule, String coursCode);
}
