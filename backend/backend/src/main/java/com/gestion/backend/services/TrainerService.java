package com.gestion.backend.services;

import com.gestion.backend.entities.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer save(Trainer trainer);

    Trainer update(Long id, Trainer trainer);

    void delete(Long id);

    Optional<Trainer> findById(Long id);

    List<Trainer> findAll();

    Optional<Trainer> findByEmail(String email);
}
