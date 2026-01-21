package com.gestion.backend.controllers.admin;

import com.gestion.backend.entities.Specialty;
import com.gestion.backend.entities.StudentGroup;
import com.gestion.backend.services.SpecialtyService;
import com.gestion.backend.services.StudentGroupService;
import com.gestion.backend.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/advanced")
@RequiredArgsConstructor
public class AdminAdvancedController {

    private final SpecialtyService specialtyService;
    private final StudentGroupService studentGroupService;
    private final SessionService sessionService;

    @GetMapping("/specialties")
    public String listSpecialties(Model model) {
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("newSpecialty", new Specialty());
        return "admin/advanced/specialties";
    }

    @PostMapping("/specialties")
    public String createSpecialty(@ModelAttribute Specialty specialty) {
        specialtyService.createSpecialty(specialty);
        return "redirect:/admin/advanced/specialties";
    }

    @GetMapping("/groups")
    public String listGroups(Model model) {
        model.addAttribute("groups", studentGroupService.getAllGroups());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("newGroup", new StudentGroup());
        return "admin/advanced/groups";
    }

    @PostMapping("/groups")
    public String createGroup(@ModelAttribute StudentGroup group) {
        studentGroupService.createGroup(group);
        return "redirect:/admin/advanced/groups";
    }

    @GetMapping("/sessions")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllSessions());
        model.addAttribute("newSession", new com.gestion.backend.entities.Session());
        return "admin/advanced/sessions";
    }

    @PostMapping("/sessions")
    public String createSession(@ModelAttribute com.gestion.backend.entities.Session session) {
        sessionService.createSession(session);
        return "redirect:/admin/advanced/sessions";
    }

    @PostMapping("/sessions/toggle/{id}")
    public String toggleSession(@PathVariable Long id) {
        sessionService.toggleActive(id);
        return "redirect:/admin/advanced/sessions";
    }

    @GetMapping("/sessions/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return "redirect:/admin/advanced/sessions";
    }
}
