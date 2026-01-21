package com.gestion.backend.dtos;

import com.gestion.backend.entities.Trainer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.stream.Collectors;

@Data
public class TrainerDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String specialty;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private java.util.List<CourseDto> courses;

    public static TrainerDto fromEntity(Trainer trainer) {
        TrainerDto dto = new TrainerDto();
        dto.setId(trainer.getId());
        dto.setName(trainer.getName());
        dto.setEmail(trainer.getEmail());
        dto.setSpecialty(trainer.getSpecialty());
        if (trainer.getCourses() != null) {
            dto.setCourses(trainer.getCourses().stream()
                    .map(CourseDto::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
