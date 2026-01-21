package com.gestion.backend.repositories;

import com.gestion.backend.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);

    List<Course> findByTitleContainingIgnoreCase(String title);

    List<Course> findByTrainerId(Long trainerId);
}
