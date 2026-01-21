package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Specialty;
import com.gestion.backend.repositories.SpecialtyRepository;
import com.gestion.backend.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    @Override
    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    @Override
    public Specialty createSpecialty(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    @Override
    public java.util.Optional<Specialty> findById(Long id) {
        return specialtyRepository.findById(id);
    }
}
