package com.gestion.backend.services;

import com.gestion.backend.entities.Grade;
import java.util.List;
import java.io.ByteArrayInputStream;

public interface ReportService {
    double calculateStudentGpa(Long studentId);

    double calculateCourseSuccessRate(Long courseId);

    ByteArrayInputStream generateGradesPdf(Long studentId);
}
