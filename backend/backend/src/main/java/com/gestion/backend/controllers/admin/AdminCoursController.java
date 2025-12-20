package com.gestion.backend.controllers.admin;

import com.gestion.backend.dtos.CoursDto;
import com.gestion.backend.services.CoursService;
import com.gestion.backend.services.FormateurService;
import com.gestion.backend.inputs.CoursInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cours")
@RequiredArgsConstructor
public class AdminCoursController {

    private final CoursService coursService;
    private final FormateurService formateurService;

    @GetMapping
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/cours/list";
    }

    @GetMapping("/create")
    public String createCoursForm(Model model) {
        model.addAttribute("coursInput", new CoursInput());
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "admin/cours/form";
    }

    @PostMapping("/create")
    public String createCours(@Valid @ModelAttribute("coursInput") CoursInput coursInput,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formateurs", formateurService.getAllFormateurs());
            return "admin/cours/form";
        }
        try {
            coursService.createCours(coursInput);
            redirectAttributes.addFlashAttribute("successMessage", "Cours créé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            model.addAttribute("formateurs", formateurService.getAllFormateurs());
            return "admin/cours/form";
        }
        return "redirect:/admin/cours";
    }

    @GetMapping("/edit/{code}")
    public String editCoursForm(@PathVariable String code, Model model) {
        CoursDto cours = coursService.getCoursByCode(code);
        CoursInput input = CoursInput.builder()
                .code(cours.code())
                .titre(cours.titre())
                .description(cours.description())
                .formateurId(cours.formateurId())
                .build();
        model.addAttribute("coursInput", input);
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        model.addAttribute("isEdit", true);
        return "admin/cours/form";
    }

    @PostMapping("/edit/{code}")
    public String updateCours(@PathVariable String code,
            @Valid @ModelAttribute("coursInput") CoursInput coursInput,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formateurs", formateurService.getAllFormateurs());
            return "admin/cours/form";
        }
        try {
            coursService.updateCours(code, coursInput);
            redirectAttributes.addFlashAttribute("successMessage", "Cours mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            model.addAttribute("formateurs", formateurService.getAllFormateurs());
            return "admin/cours/form";
        }
        return "redirect:/admin/cours";
    }

    @GetMapping("/delete/{code}")
    public String deleteCours(@PathVariable String code, RedirectAttributes redirectAttributes) {
        try {
            coursService.deleteCours(code);
            redirectAttributes.addFlashAttribute("successMessage", "Cours supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/cours";
    }
}
