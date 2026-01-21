package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.TrainerDto;
import com.gestion.backend.entities.Trainer;
import com.gestion.backend.services.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerRestController {

    private final TrainerService trainerService;

    @GetMapping
    public List<TrainerDto> getAllTrainers() {
        return trainerService.findAll().stream()
                .map(TrainerDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerDto> getTrainerById(@PathVariable Long id) {
        return trainerService.findById(id)
                .map(TrainerDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrainerDto> createTrainer(@Valid @RequestBody TrainerDto trainerDto) {
        Trainer trainer = convertToEntity(trainerDto);
        Trainer saved = trainerService.save(trainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(TrainerDto.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainerDto> updateTrainer(@PathVariable Long id, @Valid @RequestBody TrainerDto trainerDto) {
        Trainer trainer = convertToEntity(trainerDto);
        Trainer updated = trainerService.update(id, trainer);
        return ResponseEntity.ok(TrainerDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        trainerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Trainer convertToEntity(TrainerDto dto) {
        Trainer trainer = new Trainer();
        trainer.setName(dto.getName());
        trainer.setSpecialty(dto.getSpecialty());
        trainer.setEmail(dto.getEmail());
        return trainer;
    }
}
