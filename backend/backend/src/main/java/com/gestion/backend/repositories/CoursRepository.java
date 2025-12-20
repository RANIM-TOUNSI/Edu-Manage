package com.gestion.backend.repositories;

import com.gestion.backend.entities.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepository extends JpaRepository<Cours, String> {
    boolean existsByCodeAndFormateurId(String code, Long formateurId);
}
