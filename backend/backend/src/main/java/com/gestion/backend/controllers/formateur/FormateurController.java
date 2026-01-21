package com.gestion.backend.controllers.formateur;

import com.gestion.backend.entities.Trainer;
import com.gestion.backend.entities.Course;
import com.gestion.backend.services.CourseService;
import com.gestion.backend.services.TrainerService;
import com.gestion.backend.services.GradeService;
import com.gestion.backend.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/formateur")
@RequiredArgsConstructor
public class FormateurController {

    private final CourseService courseService;
    private final TrainerService trainerService;
    private final GradeService gradeService;
    private final EnrollmentService enrollmentService;

    private final com.gestion.backend.repositories.UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String username = auth.getName();
        com.gestion.backend.entities.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long trainerId = user.getTrainerId();
        if (trainerId == null) {
            // Fallback to email search if trainerId not linked in User record
            trainerId = trainerService.findByEmail(username)
                    .map(com.gestion.backend.entities.Trainer::getId)
                    .orElseThrow(() -> new RuntimeException("Trainer profile not found for user: " + username));
        }

        Trainer trainer = trainerService.findById(trainerId).orElseThrow();
        model.addAttribute("courses", courseService.findByTrainer(trainerId));
        model.addAttribute("formateurName", trainer.getName());
        return "formateur/dashboard";
    }

    @GetMapping("/course/{id}/grades")
    public String manageGrades(@PathVariable Long id, Authentication auth, Model model) {
        String username = auth.getName();
        com.gestion.backend.entities.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseService.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        // Security check: Ensure this course belongs to the logged-in trainer
        boolean isAssignedToMe = course.getTrainer() != null &&
                ((user.getTrainerId() != null && course.getTrainer().getId().equals(user.getTrainerId())) ||
                        (course.getTrainer().getEmail().equals(username)));

        if (!isAssignedToMe) {
            throw new RuntimeException("Unauthorized: You do not have access to this course.");
        }

        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollmentService.getCourseEnrollments(id));

        var grades = gradeService.getCourseGrades(id).stream()
                .collect(java.util.stream.Collectors.toMap(
                        g -> g.getStudent().getId(),
                        g -> g.getValue(),
                        (v1, v2) -> v1 // Keep first if multiple
                ));
        model.addAttribute("studentGrades", grades);
        return "formateur/course";
    }

    @PostMapping("/course/assign-grade")
    public String assignGrade(@RequestParam Long studentId, @RequestParam Long courseId, @RequestParam Double value) {
        gradeService.assignGrade(studentId, courseId, value);
        return "redirect:/formateur/course/" + courseId + "/grades";
    }
}
