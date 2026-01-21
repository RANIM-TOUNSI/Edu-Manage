package com.gestion.backend.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Grade value is required")
    @Min(0)
    @Max(20)
    private Double value;
}
