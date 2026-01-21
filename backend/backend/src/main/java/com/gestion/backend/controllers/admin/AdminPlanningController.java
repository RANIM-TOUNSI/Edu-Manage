package com.gestion.backend.controllers.admin;

import com.gestion.backend.entities.Seance;
import com.gestion.backend.services.PlanningService;
import com.gestion.backend.services.CourseService;
import com.gestion.backend.services.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/planning")
@RequiredArgsConstructor
public class AdminPlanningController {

    private final PlanningService planningService;
    private final CourseService courseService;
    private final StudentGroupService groupService;

    @GetMapping
    public String showPlanning(Model model) {
        model.addAttribute("seances", planningService.getAllSeances());
        return "admin/planning/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        com.gestion.backend.entities.Seance seance = new com.gestion.backend.entities.Seance();
        seance.setCourse(new com.gestion.backend.entities.Course());
        seance.setGroup(new com.gestion.backend.entities.StudentGroup());
        model.addAttribute("seanceInput", seance);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/planning/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("seanceInput") Seance seance) {
        try {
            planningService.createSeance(seance);
            return "redirect:/admin/planning";
        } catch (RuntimeException e) {
            return "redirect:/admin/planning/create?error=" + e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        planningService.deleteSeance(id);
        return "redirect:/admin/planning";
    }
}
