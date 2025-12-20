package com.gestion.backend.dtos;

import com.gestion.backend.entities.Note;

public record NoteDto(
        Long id,
        Double valeur,
        String etudiantMatricule,
        String coursCode) {
    public static NoteDto fromEntity(Note note) {
        if (note == null)
            return null;
        return new NoteDto(
                note.getId(),
                note.getValeur(),
                note.getEtudiant() != null ? note.getEtudiant().getMatricule() : null,
                note.getCours() != null ? note.getCours().getCode() : null);
    }
}
