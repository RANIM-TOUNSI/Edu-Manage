package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.CourseDto;
import com.gestion.backend.entities.Course;
import com.gestion.backend.entities.Trainer;
import com.gestion.backend.services.CourseService;
import com.gestion.backend.services.SpecialtyService;
import com.gestion.backend.services.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;
    private final TrainerService trainerService;
    private final SpecialtyService specialtyService;

    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.findAll().stream()
                .map(CourseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(CourseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/trainer/{trainerId}")
    public List<CourseDto> getCoursesByTrainer(@PathVariable Long trainerId) {
        return courseService.findByTrainer(trainerId).stream()
                .map(CourseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = convertToEntity(courseDto);
        Course saved = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseDto.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        Course course = convertToEntity(courseDto);
        Course updated = courseService.update(id, course);
        return ResponseEntity.ok(CourseDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Course convertToEntity(CourseDto dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        if (dto.getTrainerId() != null) {
            Trainer trainer = trainerService.findById(dto.getTrainerId())
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));
            course.setTrainer(trainer);
        }
        if (dto.getSpecialtyId() != null) {
            com.gestion.backend.entities.Specialty specialty = specialtyService.findById(dto.getSpecialtyId())
                    .orElseThrow(() -> new RuntimeException("Specialty not found"));
            course.setSpecialty(specialty);
        }
        return course;
    }
}
