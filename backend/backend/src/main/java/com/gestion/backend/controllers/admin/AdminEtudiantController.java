package com.gestion.backend.controllers.admin;

import com.gestion.backend.dtos.EtudiantDto;
import com.gestion.backend.services.EtudiantService;
import com.gestion.backend.inputs.EtudiantInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/etudiants")
@RequiredArgsConstructor
public class AdminEtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiant());
        return "admin/etudiants/list";
    }

    @GetMapping("/create")
    public String createEtudiantForm(Model model) {
        model.addAttribute("etudiantInput", new EtudiantInput());
        return "admin/etudiants/form";
    }

    @PostMapping("/create")
    public String createEtudiant(@Valid @ModelAttribute("etudiantInput") EtudiantInput etudiantInput,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/etudiants/form";
        }
        try {
            etudiantService.createEtudiant(etudiantInput);
            redirectAttributes.addFlashAttribute("successMessage", "Etudiant créé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            return "admin/etudiants/form";
        }
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/edit/{matricule}")
    public String editEtudiantForm(@PathVariable String matricule, Model model) {
        EtudiantDto etudiant = etudiantService.getEtudiantByMatricule(matricule);
        // Map DTO to Input for form binding
        EtudiantInput input = EtudiantInput.builder()
                .matricule(etudiant.matricule())
                .nom(etudiant.nom())
                .prenom(etudiant.prenom())
                .email(etudiant.email())
                .dateInscription(etudiant.dateInscription())
                .build();
        model.addAttribute("etudiantInput", input);
        model.addAttribute("isEdit", true);
        return "admin/etudiants/form";
    }

    @PostMapping("/edit/{matricule}")
    public String updateEtudiant(@PathVariable String matricule,
            @Valid @ModelAttribute("etudiantInput") EtudiantInput etudiantInput,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/etudiants/form";
        }
        try {
            etudiantService.updateEtudiant(matricule, etudiantInput);
            redirectAttributes.addFlashAttribute("successMessage", "Etudiant mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            return "admin/etudiants/form";
        }
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/delete/{matricule}")
    public String deleteEtudiant(@PathVariable String matricule, RedirectAttributes redirectAttributes) {
        try {
            etudiantService.deleteEtudiant(matricule);
            redirectAttributes.addFlashAttribute("successMessage", "Etudiant supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/etudiants";
    }
}
