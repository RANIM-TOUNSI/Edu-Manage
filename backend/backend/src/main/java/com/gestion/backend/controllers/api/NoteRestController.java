package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.NoteDto;
import com.gestion.backend.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @GetMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    public List<NoteDto> getMyNotes() {
        // Placeholder: Returning all notes for now.
        // In a real scenario, we would filter by the authenticated user's matricule.
        return noteService.getAllNotes();
    }
}
