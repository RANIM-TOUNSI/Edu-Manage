package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Trainer;
import com.gestion.backend.repositories.TrainerRepository;
import com.gestion.backend.services.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    public Trainer save(Trainer trainer) {
        if (trainerRepository.findByEmail(trainer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        Trainer existing = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        existing.setName(trainer.getName());
        existing.setEmail(trainer.getEmail());
        existing.setSpecialty(trainer.getSpecialty());
        return trainerRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByEmail(String email) {
        return trainerRepository.findByEmail(email);
    }
}
