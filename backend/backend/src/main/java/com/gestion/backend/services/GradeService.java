package com.gestion.backend.services;

import com.gestion.backend.entities.Grade;
import java.util.List;

public interface GradeService {
    Grade assignGrade(Long studentId, Long courseId, Double value);

    Grade updateGrade(Long gradeId, Double newValue);

    List<Grade> getStudentGrades(Long studentId);

    List<Grade> getCourseGrades(Long courseId);

    List<Grade> getAllGrades();
}
