package com.gestion.backend.dtos;

import com.gestion.backend.entities.Course;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDto {
    private Long id;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Long trainerId;
    private String trainerName;
    private Long specialtyId;
    private String specialtyName;

    public static CourseDto fromEntity(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        if (course.getTrainer() != null) {
            dto.setTrainerId(course.getTrainer().getId());
            dto.setTrainerName(course.getTrainer().getName());
        }
        if (course.getSpecialty() != null) {
            dto.setSpecialtyId(course.getSpecialty().getId());
            dto.setSpecialtyName(course.getSpecialty().getName());
        }
        return dto;
    }
}
