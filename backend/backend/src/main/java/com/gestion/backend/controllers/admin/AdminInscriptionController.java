package com.gestion.backend.controllers.admin;

import com.gestion.backend.services.CoursService;
import com.gestion.backend.services.EtudiantService;
import com.gestion.backend.services.InscriptionService;
import com.gestion.backend.inputs.InscriptionInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inscriptions")
@RequiredArgsConstructor
public class AdminInscriptionController {

    private final InscriptionService inscriptionService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;

    @GetMapping
    public String listInscriptions(Model model) {
        model.addAttribute("inscriptions", inscriptionService.getAllInscriptions());
        return "admin/inscriptions/list";
    }

    @GetMapping("/create")
    public String createInscriptionForm(Model model) {
        model.addAttribute("inscriptionInput", new InscriptionInput());
        model.addAttribute("etudiants", etudiantService.getAllEtudiant());
        model.addAttribute("cours", coursService.getAllCours());
        return "admin/inscriptions/form";
    }

    @PostMapping("/create")
    public String createInscription(@Valid @ModelAttribute("inscriptionInput") InscriptionInput inscriptionInput,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("etudiants", etudiantService.getAllEtudiant());
            model.addAttribute("cours", coursService.getAllCours());
            return "admin/inscriptions/form";
        }
        try {
            inscriptionService.createInscription(inscriptionInput);
            redirectAttributes.addFlashAttribute("successMessage", "Inscription créée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            model.addAttribute("etudiants", etudiantService.getAllEtudiant());
            model.addAttribute("cours", coursService.getAllCours());
            return "admin/inscriptions/form";
        }
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/delete/{id}")
    public String deleteInscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.deleteInscription(id);
            redirectAttributes.addFlashAttribute("successMessage", "Inscription supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/inscriptions";
    }
}
