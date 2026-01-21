package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Seance;
import com.gestion.backend.entities.Student;
import com.gestion.backend.repositories.SeanceRepository;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {

    private final SeanceRepository seanceRepository;
    private final StudentRepository studentRepository;
    private final com.gestion.backend.repositories.UserRepository userRepository;

    @Override
    public Seance createSeance(Seance seance) {
        checkConflicts(seance);
        return seanceRepository.save(seance);
    }

    private void checkConflicts(Seance newSeance) {
        List<Seance> existingSeances = seanceRepository.findByDate(newSeance.getDate());

        for (Seance existing : existingSeances) {
            if (isOverlapping(newSeance, existing)) {
                // Conflict 1: Same Group
                if (existing.getGroup().getId().equals(newSeance.getGroup().getId())) {
                    throw new RuntimeException(
                            "Conflict: Group " + existing.getGroup().getName() + " already has a class at this time.");
                }
                // Conflict 2: Same Trainer
                if (existing.getCourse().getTrainer() != null && newSeance.getCourse().getTrainer() != null) {
                    if (existing.getCourse().getTrainer().getId().equals(newSeance.getCourse().getTrainer().getId())) {
                        throw new RuntimeException("Conflict: Trainer " + existing.getCourse().getTrainer().getName()
                                + " is already teaching at this time.");
                    }
                }
                // Conflict 3: Same Room
                if (existing.getRoom().equalsIgnoreCase(newSeance.getRoom())) {
                    throw new RuntimeException("Conflict: Room " + existing.getRoom() + " is occupied at this time.");
                }
            }
        }
    }

    private boolean isOverlapping(Seance s1, Seance s2) {
        return !s1.getEndTime().isBefore(s2.getStartTime()) && !s1.getStartTime().isAfter(s2.getEndTime());
    }

    @Override
    public List<Seance> getSeancesByGroup(Long groupId) {
        return seanceRepository.findByGroupId(groupId);
    }

    @Override
    public List<Seance> getSeancesByTrainer(Long trainerId) {
        return seanceRepository.findByCourseTrainerId(trainerId);
    }

    @Override
    public List<Seance> getSeancesByStudent(String username) {
        com.gestion.backend.entities.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStudentId() == null) {
            return List.of();
        }

        Student student = studentRepository.findById(user.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        if (student.getGroup() == null) {
            return List.of();
        }
        return seanceRepository.findByGroupId(student.getGroup().getId());
    }

    @Override
    public void deleteSeance(Long id) {
        seanceRepository.deleteById(id);
    }

    @Override
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }
}
