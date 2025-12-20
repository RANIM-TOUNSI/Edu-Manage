package com.gestion.backend.dtos;

import com.gestion.backend.entities.Etudiant;

import java.time.LocalDate;

public record EtudiantDto(
        String matricule,
        String nom,
        String prenom,
        String email,
        LocalDate dateInscription
) {

    public static EtudiantDto fromEntity(Etudiant etudiant) {
        if (etudiant == null) return null;
        return new EtudiantDto(
                etudiant.getMatricule(),
                etudiant.getNom(),
                etudiant.getPrenom(),
                etudiant.getEmail(),
                etudiant.getDateInscription()
        );
    }
}

