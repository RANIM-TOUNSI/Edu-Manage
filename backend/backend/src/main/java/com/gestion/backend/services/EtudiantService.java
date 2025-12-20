package com.gestion.backend.services;

import com.gestion.backend.dtos.EtudiantDto;
import com.gestion.backend.inputs.EtudiantInput;

import java.util.List;

public interface EtudiantService {
    EtudiantDto createEtudiant(EtudiantInput etudiant);

    EtudiantDto updateEtudiant(String matricule, EtudiantInput etudiant);

    EtudiantDto getEtudiantByMatricule (String matricule);

    List<EtudiantDto> getAllEtudiant();

    void deleteEtudiant(String matricule);
}
