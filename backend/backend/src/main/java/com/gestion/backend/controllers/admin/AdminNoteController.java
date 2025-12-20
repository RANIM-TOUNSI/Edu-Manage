package com.gestion.backend.controllers.admin;

import com.gestion.backend.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notes")
@RequiredArgsConstructor
public class AdminNoteController {

    private final NoteService noteService;

    @GetMapping
    public String listNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "admin/notes/list"; // Read-only view
    }
}
