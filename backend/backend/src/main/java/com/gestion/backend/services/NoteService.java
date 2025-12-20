package com.gestion.backend.services;

import com.gestion.backend.dtos.NoteDto;
import com.gestion.backend.inputs.NoteInput;
import java.util.List;

public interface NoteService {
    NoteDto createNote(NoteInput input, Long formateurId); // Business rule: Formateur creates note

    NoteDto getNoteById(Long id);

    List<NoteDto> getAllNotes();

    void deleteNote(Long id);
}
