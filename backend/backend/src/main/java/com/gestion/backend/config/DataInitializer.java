package com.gestion.backend.config;

import com.gestion.backend.entities.*;
import com.gestion.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final TrainerRepository trainerRepository;
        private final SpecialtyRepository specialtyRepository;
        private final SessionRepository sessionRepository;
        private final StudentGroupRepository groupRepository;
        private final CourseRepository courseRepository;
        private final SeanceRepository seanceRepository;
        private final StudentRepository studentRepository;
        private final EnrollmentRepository enrollmentRepository;
        private final GradeRepository gradeRepository;

        @Override
        public void run(String... args) {
                // 1. Default Admin
                if (userRepository.findByUsername("admin").isEmpty()) {
                        User admin = User.builder()
                                        .username("admin")
                                        .password(passwordEncoder.encode("admin123"))
                                        .role(User.Role.ADMIN)
                                        .build();
                        userRepository.save(admin);
                        log.info("Default admin user created: admin / admin123");
                }

                // 2. Default Trainer and Specialty
                if (userRepository.findByUsername("trainer").isEmpty()) {
                        Specialty softwareEng = Specialty.builder()
                                        .name("Software Engineering")
                                        .description("Focuses on software development and design")
                                        .build();
                        softwareEng = specialtyRepository.save(softwareEng);

                        Trainer trainerEntity = Trainer.builder()
                                        .name("Alice Smith")
                                        .email("trainer@edumanage.com")
                                        .specialty("Computer Science")
                                        .build();
                        trainerEntity = trainerRepository.save(trainerEntity);

                        User trainerUser = User.builder()
                                        .username("trainer")
                                        .password(passwordEncoder.encode("trainer123"))
                                        .role(User.Role.TRAINER)
                                        .trainerId(trainerEntity.getId())
                                        .build();
                        userRepository.save(trainerUser);

                        // 3. Default Session
                        Session fall2026 = Session.builder()
                                        .title("Fall 2026")
                                        .startDate(LocalDate.of(2026, 9, 1))
                                        .endDate(LocalDate.of(2026, 12, 20))
                                        .active(true)
                                        .build();
                        sessionRepository.save(fall2026);

                        // 4. Default Group
                        StudentGroup groupA = StudentGroup.builder()
                                        .name("Group CS-2026-A")
                                        .specialty(softwareEng)
                                        .build();
                        groupA = groupRepository.save(groupA);

                        // 5. Assigned Courses
                        Course javaCourse = Course.builder()
                                        .code("CS-101")
                                        .title("Java Programming")
                                        .description("Core concepts of Java language")
                                        .trainer(trainerEntity)
                                        .specialty(softwareEng)
                                        .build();
                        javaCourse = courseRepository.save(javaCourse);

                        // 6. Schedule (Seances)
                        Seance seance1 = Seance.builder()
                                        .course(javaCourse)
                                        .group(groupA)
                                        .date(LocalDate.now().plusDays(1))
                                        .startTime(LocalTime.of(9, 0))
                                        .endTime(LocalTime.of(12, 0))
                                        .room("Room 101")
                                        .build();
                        seanceRepository.save(seance1);

                        // 7. Default Student
                        Student studentEntity = Student.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .email("student@edumanage.com")
                                        .matricule("STU-001")
                                        .registrationDate(LocalDate.now())
                                        .group(groupA)
                                        .build();
                        studentEntity = studentRepository.save(studentEntity);

                        User studentUser = User.builder()
                                        .username("student")
                                        .password(passwordEncoder.encode("student123"))
                                        .role(User.Role.STUDENT)
                                        .studentId(studentEntity.getId())
                                        .build();
                        userRepository.save(studentUser);

                        // 8. Default Enrollment and Grade
                        Enrollment enrollment = Enrollment.builder()
                                        .student(studentEntity)
                                        .course(javaCourse)
                                        .enrollmentDate(LocalDate.now())
                                        .build();
                        enrollmentRepository.save(enrollment);

                        Grade grade = Grade.builder()
                                        .student(studentEntity)
                                        .course(javaCourse)
                                        .value(15.5)
                                        .build();
                        gradeRepository.save(grade);

                        log.info("Sample data initialized: Trainer Alice Smith and Student John Doe created.");
                }
        }
}
