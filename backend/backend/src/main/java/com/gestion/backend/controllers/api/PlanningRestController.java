package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.SeanceDto;
import com.gestion.backend.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planning")
@RequiredArgsConstructor
public class PlanningRestController {

    private final PlanningService planningService;

    @GetMapping("/my-schedule")
    public List<SeanceDto> getMySchedule(Authentication auth) {
        return planningService.getSeancesByStudent(auth.getName()).stream()
                .map(SeanceDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/trainer/{trainerId}")
    public List<SeanceDto> getTrainerSchedule(@PathVariable Long trainerId) {
        return planningService.getSeancesByTrainer(trainerId).stream()
                .map(SeanceDto::fromEntity)
                .collect(Collectors.toList());
    }
}
