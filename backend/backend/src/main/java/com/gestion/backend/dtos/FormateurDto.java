package com.gestion.backend.dtos;

import com.gestion.backend.entities.Formateur;

public record FormateurDto(
        Long id,
        String nom,
        String specialite,
        String email) {
    public static FormateurDto fromEntity(Formateur formateur) {
        if (formateur == null)
            return null;
        return new FormateurDto(
                formateur.getId(),
                formateur.getNom(),
                formateur.getSpecialite(),
                formateur.getEmail());
    }
}
