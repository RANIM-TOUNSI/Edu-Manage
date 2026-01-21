package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.EnrollmentDto;
import com.gestion.backend.dtos.EnrollmentRequest;
import com.gestion.backend.entities.Enrollment;
import com.gestion.backend.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.enroll(request.getStudentId(), request.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentDto> getStudentEnrollments(@PathVariable Long studentId) {
        return enrollmentService.getStudentEnrollments(studentId).stream()
                .map(EnrollmentDto::fromEntity)
                .collect(Collectors.toList());
    }
}
