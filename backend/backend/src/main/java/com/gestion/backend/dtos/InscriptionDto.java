package com.gestion.backend.dtos;

import com.gestion.backend.entities.Inscription;
import java.time.LocalDate;

public record InscriptionDto(
        Long id,
        LocalDate date,
        String etudiantMatricule,
        String coursCode) {
    public static InscriptionDto fromEntity(Inscription inscription) {
        if (inscription == null)
            return null;
        return new InscriptionDto(
                inscription.getId(),
                inscription.getDate(),
                inscription.getEtudiant() != null ? inscription.getEtudiant().getMatricule() : null,
                inscription.getCours() != null ? inscription.getCours().getCode() : null);
    }
}
