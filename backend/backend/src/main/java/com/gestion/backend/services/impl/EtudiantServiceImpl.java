package com.gestion.backend.services.impl;

import com.gestion.backend.dtos.EtudiantDto;
import com.gestion.backend.entities.Etudiant;
import com.gestion.backend.repositories.EtudiantRepository;
import com.gestion.backend.services.EtudiantService;
import com.gestion.backend.inputs.EtudiantInput;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {
    private final EtudiantRepository etudiantRepository;

        public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {
            this.etudiantRepository = etudiantRepository;
        }

        // Méthode de validation
        private void validateEtudiantInput(EtudiantInput input) {
            if (input.getMatricule() == null || input.getMatricule().isBlank()) {
                throw new IllegalArgumentException("Matricule obligatoire");
            }

            if (input.getNom() == null || input.getNom().isBlank()) {
                throw new IllegalArgumentException("Nom obligatoire");
            }

            if (input.getPrenom() == null || input.getPrenom().isBlank()) {
                throw new IllegalArgumentException("Prénom obligatoire");
            }

            if (input.getEmail() == null || input.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email obligatoire");
            }

            if (!input.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("Email invalide");
            }

            etudiantRepository.findByMatricule(input.getMatricule())
                    .ifPresent(e -> { throw new IllegalArgumentException("Matricule déjà existant"); });
        }

    @Override
    public EtudiantDto createEtudiant(EtudiantInput etudiantInput) {
        validateEtudiantInput(etudiantInput);
        Etudiant etudiantEntity = Etudiant.builder()
                .matricule(etudiantInput.getMatricule())
                .nom(etudiantInput.getNom())
                .prenom(etudiantInput.getPrenom())
                .email(etudiantInput.getEmail())
                .dateInscription(etudiantInput.getDateInscription() != null ? etudiantInput.getDateInscription() : LocalDate.now())
                .build();

        Etudiant savedEtudiant = etudiantRepository.save(etudiantEntity);
        return EtudiantDto.fromEntity(savedEtudiant);
    }

    @Override
    public EtudiantDto updateEtudiant(String matricule, EtudiantInput input) {
        Etudiant etudiant = etudiantRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        etudiant.setNom(input.getNom());
        etudiant.setPrenom(input.getPrenom());
        etudiant.setEmail(input.getEmail());
        etudiant.setDateInscription(input.getDateInscription());

        Etudiant saved = etudiantRepository.save(etudiant);
        return EtudiantDto.fromEntity(saved);
    }


    @Override
    public EtudiantDto getEtudiantByMatricule(String matricule) {
        return null;
    }

    @Override
    public List<EtudiantDto> getAllEtudiant() {
        return etudiantRepository.findAll()
                .stream()
                .map(EtudiantDto::fromEntity)
                .toList();
    }

    @Override
    public void deleteEtudiant(String matricule) {
        Etudiant etudiant = etudiantRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        etudiantRepository.delete(etudiant);
    }

}


