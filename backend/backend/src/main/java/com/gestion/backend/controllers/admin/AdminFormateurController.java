package com.gestion.backend.controllers.admin;

import com.gestion.backend.dtos.FormateurDto;
import com.gestion.backend.services.FormateurService;
import com.gestion.backend.inputs.FormateurInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/formateurs")
@RequiredArgsConstructor
public class AdminFormateurController {

    private final FormateurService formateurService;

    @GetMapping
    public String listFormateurs(Model model) {
        model.addAttribute("formateurs", formateurService.getAllFormateurs());
        return "admin/formateurs/list";
    }

    @GetMapping("/create")
    public String createFormateurForm(Model model) {
        model.addAttribute("formateurInput", new FormateurInput());
        return "admin/formateurs/form";
    }

    @PostMapping("/create")
    public String createFormateur(@Valid @ModelAttribute("formateurInput") FormateurInput formateurInput,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/formateurs/form";
        }
        try {
            formateurService.createFormateur(formateurInput);
            redirectAttributes.addFlashAttribute("successMessage", "Formateur créé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            return "admin/formateurs/form";
        }
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/edit/{id}")
    public String editFormateurForm(@PathVariable Long id, Model model) {
        FormateurDto formateur = formateurService.getFormateurById(id);
        FormateurInput input = FormateurInput.builder()
                .nom(formateur.nom())
                .specialite(formateur.specialite())
                .email(formateur.email())
                .build();
        model.addAttribute("formateurInput", input);
        model.addAttribute("formateurId", id);
        model.addAttribute("isEdit", true);
        return "admin/formateurs/form";
    }

    @PostMapping("/edit/{id}")
    public String updateFormateur(@PathVariable Long id,
            @Valid @ModelAttribute("formateurInput") FormateurInput formateurInput,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/formateurs/form";
        }
        try {
            formateurService.updateFormateur(id, formateurInput);
            redirectAttributes.addFlashAttribute("successMessage", "Formateur mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            return "admin/formateurs/form";
        }
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/delete/{id}")
    public String deleteFormateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            formateurService.deleteFormateur(id);
            redirectAttributes.addFlashAttribute("successMessage", "Formateur supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/formateurs";
    }
}
