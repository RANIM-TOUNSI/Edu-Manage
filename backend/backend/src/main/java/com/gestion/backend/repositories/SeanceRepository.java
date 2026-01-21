package com.gestion.backend.repositories;

import com.gestion.backend.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByGroupId(Long groupId);

    List<Seance> findByCourseTrainerId(Long trainerId);

    List<Seance> findByDate(LocalDate date);
}
