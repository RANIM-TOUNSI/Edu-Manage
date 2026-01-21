package com.gestion.backend.services;

import com.gestion.backend.entities.Course;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course save(Course course);

    Course update(Long id, Course course);

    void delete(Long id);

    Optional<Course> findById(Long id);

    List<Course> findAll();

    Optional<Course> findByCode(String code);

    List<Course> findByTrainer(Long trainerId);
}
