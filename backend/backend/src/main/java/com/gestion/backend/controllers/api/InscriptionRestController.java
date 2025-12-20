package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.InscriptionDto;
import com.gestion.backend.inputs.InscriptionInput;
import com.gestion.backend.services.InscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionRestController {

    private final InscriptionService inscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<InscriptionDto> register(@Valid @RequestBody InscriptionInput inscriptionInput) {
        // Note: Ideally, we should verify that the input matricule matches the
        // authenticated user
        InscriptionDto created = inscriptionService.createInscription(inscriptionInput);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'ADMIN')")
    public ResponseEntity<Void> unregister(@PathVariable Long id) {
        // Note: Ideally, check ownership if user is ETUDIANT
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}
