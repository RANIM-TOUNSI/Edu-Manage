package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Course;
import com.gestion.backend.entities.Grade;
import com.gestion.backend.entities.Student;
import com.gestion.backend.repositories.CourseRepository;
import com.gestion.backend.repositories.GradeRepository;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.services.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public Grade assignGrade(Long studentId, Long courseId, Double value) {
        validateGradeValue(value);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Grade grade = Grade.builder()
                .student(student)
                .course(course)
                .value(value)
                .build();

        return gradeRepository.save(grade);
    }

    @Override
    public Grade updateGrade(Long gradeId, Double newValue) {
        validateGradeValue(newValue);

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));
        grade.setValue(newValue);
        return gradeRepository.save(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getStudentGrades(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getCourseGrades(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    private void validateGradeValue(Double value) {
        if (value < 0 || value > 20) {
            throw new RuntimeException("Grade must be between 0 and 20");
        }
    }
}
