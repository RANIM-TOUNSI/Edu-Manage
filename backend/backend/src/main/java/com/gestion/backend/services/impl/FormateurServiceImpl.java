package com.gestion.backend.services.impl;

import com.gestion.backend.dtos.FormateurDto;
import com.gestion.backend.entities.Formateur;
import com.gestion.backend.repositories.FormateurRepository;
import com.gestion.backend.services.FormateurService;
import com.gestion.backend.inputs.FormateurInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormateurServiceImpl implements FormateurService {

    private final FormateurRepository formateurRepository;

    @Override
    @Transactional
    public FormateurDto createFormateur(FormateurInput input) {
        Formateur formateur = Formateur.builder()
                .nom(input.getNom())
                .specialite(input.getSpecialite())
                .email(input.getEmail())
                .build();
        return FormateurDto.fromEntity(formateurRepository.save(formateur));
    }

    @Override
    @Transactional
    public FormateurDto updateFormateur(Long id, FormateurInput input) {
        Formateur formateur = formateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formateur not found"));
        formateur.setNom(input.getNom());
        formateur.setSpecialite(input.getSpecialite());
        formateur.setEmail(input.getEmail());
        return FormateurDto.fromEntity(formateurRepository.save(formateur));
    }

    @Override
    @Transactional(readOnly = true)
    public FormateurDto getFormateurById(Long id) {
        return formateurRepository.findById(id)
                .map(FormateurDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Formateur not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormateurDto> getAllFormateurs() {
        return formateurRepository.findAll().stream()
                .map(FormateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }
}
