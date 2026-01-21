package com.gestion.backend;

import com.gestion.backend.dtos.SeanceDto;
import com.gestion.backend.entities.Cours;
import com.gestion.backend.entities.Formateur;
import com.gestion.backend.inputs.SeanceInput;
import com.gestion.backend.repositories.CoursRepository;
import com.gestion.backend.repositories.FormateurRepository;
import com.gestion.backend.repositories.advanced.SeanceRepository;
import com.gestion.backend.services.PlanningService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PlanningIntegrationTest {

    @Autowired
    private PlanningService planningService;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private FormateurRepository formateurRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    private Cours testCours;

    @BeforeEach
    void setUp() {
        // Prepare Data
        Formateur prof = Formateur.builder()
                .nom("Test Prof")
                .email("prof@test.com")
                .specialite("Java")
                .build();
        formateurRepository.save(prof);

        testCours = Cours.builder()
                .code("JAVA101")
                .titre("Java Basics")
                .description("Intro")
                .formateur(prof) // Assign Prof
                .build();
        coursRepository.save(testCours);
    }

    @Test
    void testCreateSeanceSuccess() {
        SeanceInput input = new SeanceInput();
        input.setCoursCode(testCours.getCode());
        input.setSalle("A101");
        input.setDateHeureDebut(LocalDateTime.now().plusHours(1));
        input.setDateHeureFin(LocalDateTime.now().plusHours(3));

        SeanceDto created = planningService.createSeance(input);
        Assertions.assertNotNull(created.getId());
    }

    @Test
    void testRoomConflict() {
        // 1. Create First Seance
        SeanceInput input1 = new SeanceInput();
        input1.setCoursCode(testCours.getCode());
        input1.setSalle("A101");
        input1.setDateHeureDebut(LocalDateTime.now().plusHours(10));
        input1.setDateHeureFin(LocalDateTime.now().plusHours(12));
        planningService.createSeance(input1);

        // 2. Try same room same time
        SeanceInput input2 = new SeanceInput();
        input2.setCoursCode(testCours.getCode());
        input2.setSalle("A101"); // CONFLICT
        input2.setDateHeureDebut(LocalDateTime.now().plusHours(10));
        input2.setDateHeureFin(LocalDateTime.now().plusHours(12));

        Assertions.assertThrows(RuntimeException.class, () -> {
            planningService.createSeance(input2);
        });
    }
}
