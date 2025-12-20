package com.gestion.backend.services.impl;

import com.gestion.backend.dtos.InscriptionDto;
import com.gestion.backend.entities.Cours;
import com.gestion.backend.entities.Etudiant;
import com.gestion.backend.entities.Inscription;
import com.gestion.backend.repositories.CoursRepository;
import com.gestion.backend.repositories.EtudiantRepository;
import com.gestion.backend.repositories.InscriptionRepository;
import com.gestion.backend.services.InscriptionService;
import com.gestion.backend.inputs.InscriptionInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    @Override
    @Transactional
    public InscriptionDto createInscription(InscriptionInput input) {
        // Business Rule: Student cannot be enrolled twice in the same course
        if (inscriptionRepository.existsByEtudiantMatriculeAndCoursCode(input.getEtudiantMatricule(),
                input.getCoursCode())) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Etudiant etudiant = etudiantRepository.findByMatricule(input.getEtudiantMatricule())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Cours cours = coursRepository.findById(input.getCoursCode())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Inscription inscription = Inscription.builder()
                .date(LocalDate.now())
                .etudiant(etudiant)
                .cours(cours)
                .build();
        return InscriptionDto.fromEntity(inscriptionRepository.save(inscription));
    }

    @Override
    @Transactional(readOnly = true)
    public InscriptionDto getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .map(InscriptionDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Inscription not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDto> getAllInscriptions() {
        return inscriptionRepository.findAll().stream()
                .map(InscriptionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteInscription(Long id) {
        inscriptionRepository.deleteById(id);
    }
}
