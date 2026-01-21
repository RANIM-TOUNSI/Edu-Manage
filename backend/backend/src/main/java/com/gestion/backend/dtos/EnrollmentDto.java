package com.gestion.backend.dtos;

import com.gestion.backend.entities.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String courseTitle;
    private LocalDate enrollmentDate;

    public static EnrollmentDto fromEntity(Enrollment enrollment) {
        return EnrollmentDto.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .build();
    }
}
