package com.gestion.backend.repositories;

import com.gestion.backend.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByEmail(String email);

    List<Trainer> findByNameContainingIgnoreCase(String name);
}
