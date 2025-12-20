package com.gestion.backend.services.impl;

import com.gestion.backend.dtos.NoteDto;
import com.gestion.backend.entities.Cours;
import com.gestion.backend.entities.Etudiant;
import com.gestion.backend.entities.Note;
import com.gestion.backend.repositories.CoursRepository;
import com.gestion.backend.repositories.EtudiantRepository;
import com.gestion.backend.repositories.NoteRepository;
import com.gestion.backend.services.NoteService;
import com.gestion.backend.inputs.NoteInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    @Override
    @Transactional
    public NoteDto createNote(NoteInput input, Long formateurId) {
        // Business Rule: A trainer can only assign grades for their own courses
        Cours cours = coursRepository.findById(input.getCoursCode())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (cours.getFormateur() == null || !cours.getFormateur().getId().equals(formateurId)) {
            throw new RuntimeException("Trainer is not authorized to grade this course");
        }

        Etudiant etudiant = etudiantRepository.findByMatricule(input.getEtudiantMatricule())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Note note = Note.builder()
                .valeur(input.getValeur())
                .etudiant(etudiant)
                .cours(cours)
                .build();
        return NoteDto.fromEntity(noteRepository.save(note));
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDto getNoteById(Long id) {
        return noteRepository.findById(id)
                .map(NoteDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(NoteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
