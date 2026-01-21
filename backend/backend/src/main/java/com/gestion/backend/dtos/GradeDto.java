package com.gestion.backend.dtos;

import com.gestion.backend.entities.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeDto {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String courseTitle;
    private Double value;

    public static GradeDto fromEntity(Grade grade) {
        return GradeDto.builder()
                .id(grade.getId())
                .studentId(grade.getStudent().getId())
                .courseId(grade.getCourse().getId())
                .courseTitle(grade.getCourse().getTitle())
                .value(grade.getValue())
                .build();
    }
}
