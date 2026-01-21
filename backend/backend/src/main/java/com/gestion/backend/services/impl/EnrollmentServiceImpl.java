package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Course;
import com.gestion.backend.entities.Enrollment;
import com.gestion.backend.entities.Student;
import com.gestion.backend.repositories.CourseRepository;
import com.gestion.backend.repositories.EnrollmentRepository;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.repositories.UserRepository;
import com.gestion.backend.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

        private final EnrollmentRepository enrollmentRepository;
        private final StudentRepository studentRepository;
        private final CourseRepository courseRepository;
        private final UserRepository userRepository;
        private final com.gestion.backend.services.EmailService emailService;

        @Override
        public Enrollment enroll(Long studentId, Long courseId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new RuntimeException("Student not found"));
                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new RuntimeException("Course not found"));

                // Validate no duplicate enrollment
                boolean alreadyEnrolled = enrollmentRepository.findByStudentId(studentId).stream()
                                .anyMatch(e -> e.getCourse().getId().equals(courseId));

                if (alreadyEnrolled) {
                        throw new RuntimeException("Student is already enrolled in this course");
                }

                Enrollment enrollment = Enrollment.builder()
                                .student(student)
                                .course(course)
                                .enrollmentDate(LocalDate.now())
                                .build();

                Enrollment saved = enrollmentRepository.save(enrollment);

                // Notify Student
                emailService.sendSimpleEmail(student.getEmail(), "Confirmation d'inscription",
                                "Bonjour " + student.getFirstName() + ", vous êtes inscrit au cours: "
                                                + course.getTitle());

                // Notify Trainer
                if (course.getTrainer() != null) {
                        emailService.sendSimpleEmail(course.getTrainer().getEmail(), "Nouvelle inscription",
                                        "L'étudiant " + student.getFirstName() + " " + student.getLastName()
                                                        + " s'est inscrit à votre cours: " + course.getTitle());
                }

                return saved;
        }

        @Override
        public void cancelEnrollment(Long enrollmentId) {
                Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

                enrollmentRepository.deleteById(enrollmentId);

                // Notify Trainer
                if (enrollment.getCourse().getTrainer() != null) {
                        emailService.sendSimpleEmail(enrollment.getCourse().getTrainer().getEmail(),
                                        "Annulation d'inscription",
                                        "L'étudiant " + enrollment.getStudent().getFirstName()
                                                        + " s'est désinscrit de votre cours: "
                                                        + enrollment.getCourse().getTitle());
                }
        }

        @Override
        @Transactional(readOnly = true)
        public List<Enrollment> getStudentEnrollments(Long studentId) {
                return enrollmentRepository.findByStudentId(studentId);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Enrollment> getEnrollmentsByUsername(String username) {
                com.gestion.backend.entities.User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found: " + username));

                if (user.getStudentId() == null) {
                        return java.util.Collections.emptyList();
                }

                return enrollmentRepository.findByStudentId(user.getStudentId());
        }

        @Override
        @Transactional(readOnly = true)
        public List<Enrollment> getCourseEnrollments(Long courseId) {
                return enrollmentRepository.findByCourseId(courseId);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Enrollment> getAllEnrollments() {
                return enrollmentRepository.findAll();
        }
}
