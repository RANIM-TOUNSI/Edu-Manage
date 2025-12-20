package com.gestion.backend.dtos;

import com.gestion.backend.entities.Cours;

public record CoursDto(
        String code,
        String titre,
        String description,
        Long formateurId,
        String formateurNom) {
    public static CoursDto fromEntity(Cours cours) {
        if (cours == null)
            return null;
        return new CoursDto(
                cours.getCode(),
                cours.getTitre(),
                cours.getDescription(),
                cours.getFormateur() != null ? cours.getFormateur().getId() : null,
                cours.getFormateur() != null ? cours.getFormateur().getNom() : null);
    }
}
