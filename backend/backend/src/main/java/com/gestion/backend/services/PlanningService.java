package com.gestion.backend.services;

import com.gestion.backend.entities.Seance;
import java.util.List;
import java.time.LocalDate;

public interface PlanningService {
    Seance createSeance(Seance seance);

    List<Seance> getSeancesByGroup(Long groupId);

    List<Seance> getSeancesByTrainer(Long trainerId);

    List<Seance> getSeancesByStudent(String username);

    void deleteSeance(Long id);

    List<Seance> getAllSeances();
}
