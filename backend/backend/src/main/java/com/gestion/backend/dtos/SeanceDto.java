package com.gestion.backend.dtos;

import com.gestion.backend.entities.Seance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceDto {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private Long groupId;
    private String groupName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;

    public static SeanceDto fromEntity(Seance seance) {
        return SeanceDto.builder()
                .id(seance.getId())
                .courseId(seance.getCourse().getId())
                .courseTitle(seance.getCourse().getTitle())
                .groupId(seance.getGroup().getId())
                .groupName(seance.getGroup().getName())
                .date(seance.getDate())
                .startTime(seance.getStartTime())
                .endTime(seance.getEndTime())
                .room(seance.getRoom())
                .build();
    }
}
