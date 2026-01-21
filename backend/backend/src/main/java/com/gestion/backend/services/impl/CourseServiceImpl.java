package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Course;
import com.gestion.backend.repositories.CourseRepository;
import com.gestion.backend.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Course save(Course course) {
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            course.setCode("CRS-" + System.currentTimeMillis());
        }
        Optional<Course> existing = courseRepository.findByCode(course.getCode());
        if (existing.isPresent() && (course.getId() == null || !existing.get().getId().equals(course.getId()))) {
            throw new RuntimeException("Course code already exists");
        }
        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, Course course) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        existing.setTitle(course.getTitle());
        existing.setCode(course.getCode());
        existing.setDescription(course.getDescription());
        existing.setTrainer(course.getTrainer());
        existing.setSpecialty(course.getSpecialty());
        return courseRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findByTrainer(Long trainerId) {
        return courseRepository.findByTrainerId(trainerId);
    }
}
