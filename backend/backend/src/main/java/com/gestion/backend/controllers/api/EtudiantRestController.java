package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.EtudiantDto;
import com.gestion.backend.inputs.EtudiantInput;
import com.gestion.backend.services.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public List<EtudiantDto> getAllEtudiants() {
        return etudiantService.getAllEtudiant();
    }

    @GetMapping("/{matricule}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<EtudiantDto> getEtudiant(@PathVariable String matricule) {
        return ResponseEntity.ok(etudiantService.getEtudiantByMatricule(matricule));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantDto> createEtudiant(@Valid @RequestBody EtudiantInput etudiantInput) {
        EtudiantDto created = etudiantService.createEtudiant(etudiantInput);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantDto> updateEtudiant(@PathVariable String matricule,
            @Valid @RequestBody EtudiantInput etudiantInput) {
        EtudiantDto updated = etudiantService.updateEtudiant(matricule, etudiantInput);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable String matricule) {
        etudiantService.deleteEtudiant(matricule);
        return ResponseEntity.noContent().build();
    }
}
