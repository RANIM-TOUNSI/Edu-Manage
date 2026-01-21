package com.gestion.backend.services;

import com.gestion.backend.entities.Specialty;
import java.util.List;

public interface SpecialtyService {
    List<Specialty> getAllSpecialties();

    Specialty createSpecialty(Specialty specialty);

    java.util.Optional<Specialty> findById(Long id);
}
