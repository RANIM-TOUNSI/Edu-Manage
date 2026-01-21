package com.gestion.backend.controllers.admin;

import com.gestion.backend.entities.Trainer;
import com.gestion.backend.services.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/formateurs")
@RequiredArgsConstructor
public class AdminTrainerController {

    private final TrainerService trainerService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("formateurs", trainerService.findAll());
        return "admin/formateurs/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("formateurInput", new Trainer());
        model.addAttribute("isEdit", false);
        return "admin/formateurs/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("formateurInput") Trainer trainer) {
        trainerService.save(trainer);
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("formateurInput", trainerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found")));
        model.addAttribute("formateurId", id);
        model.addAttribute("isEdit", true);
        return "admin/formateurs/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("formateurInput") Trainer trainer) {
        trainerService.update(id, trainer);
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        trainerService.delete(id);
        return "redirect:/admin/formateurs";
    }
}
