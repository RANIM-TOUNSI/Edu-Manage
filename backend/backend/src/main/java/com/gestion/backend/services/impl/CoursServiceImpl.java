package com.gestion.backend.services.impl;

import com.gestion.backend.dtos.CoursDto;
import com.gestion.backend.entities.Cours;
import com.gestion.backend.entities.Formateur;
import com.gestion.backend.repositories.CoursRepository;
import com.gestion.backend.repositories.FormateurRepository;
import com.gestion.backend.services.CoursService;
import com.gestion.backend.inputs.CoursInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoursServiceImpl implements CoursService {

    private final CoursRepository coursRepository;
    private final FormateurRepository formateurRepository;

    @Override
    @Transactional
    public CoursDto createCours(CoursInput input) {
        Formateur formateur = null;
        if (input.getFormateurId() != null) {
            formateur = formateurRepository.findById(input.getFormateurId())
                    .orElseThrow(() -> new RuntimeException("Formateur not found"));
        }

        Cours cours = Cours.builder()
                .code(input.getCode())
                .titre(input.getTitre())
                .description(input.getDescription())
                .formateur(formateur)
                .build();
        return CoursDto.fromEntity(coursRepository.save(cours));
    }

    @Override
    @Transactional
    public CoursDto updateCours(String code, CoursInput input) {
        Cours cours = coursRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Cours not found"));

        if (input.getFormateurId() != null) {
            Formateur formateur = formateurRepository.findById(input.getFormateurId())
                    .orElseThrow(() -> new RuntimeException("Formateur not found"));
            cours.setFormateur(formateur);
        }

        cours.setTitre(input.getTitre());
        cours.setDescription(input.getDescription());
        return CoursDto.fromEntity(coursRepository.save(cours));
    }

    @Override
    @Transactional(readOnly = true)
    public CoursDto getCoursByCode(String code) {
        return coursRepository.findById(code)
                .map(CoursDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Cours not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursDto> getAllCours() {
        return coursRepository.findAll().stream()
                .map(CoursDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCours(String code) {
        // Business rule: Deletion of course deletes associated inscriptions.
        // Handled by CascadeType.ALL in Cours entity for 'inscriptions' list.
        coursRepository.deleteById(code);
    }
}
