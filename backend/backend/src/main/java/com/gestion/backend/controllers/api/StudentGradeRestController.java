package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.GradeDto;
import com.gestion.backend.dtos.GradeRequest;
import com.gestion.backend.entities.Grade;
import com.gestion.backend.services.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class StudentGradeRestController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<Grade> assignGrade(@Valid @RequestBody GradeRequest request) {
        Grade grade = gradeService.assignGrade(request.getStudentId(), request.getCourseId(), request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(grade);
    }

    @GetMapping("/student/{studentId}")
    public List<GradeDto> getStudentGrades(@PathVariable Long studentId) {
        return gradeService.getStudentGrades(studentId).stream()
                .map(GradeDto::fromEntity)
                .collect(Collectors.toList());
    }
}
