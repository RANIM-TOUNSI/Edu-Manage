package com.gestion.backend.services;

import com.gestion.backend.entities.Enrollment;
import java.util.List;

public interface EnrollmentService {
    Enrollment enroll(Long studentId, Long courseId);

    void cancelEnrollment(Long enrollmentId);

    List<Enrollment> getStudentEnrollments(Long studentId);

    List<Enrollment> getEnrollmentsByUsername(String username);

    List<Enrollment> getCourseEnrollments(Long courseId);

    List<Enrollment> getAllEnrollments();
}
