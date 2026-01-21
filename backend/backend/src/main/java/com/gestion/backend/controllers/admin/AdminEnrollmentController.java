package com.gestion.backend.controllers.admin;

import com.gestion.backend.services.EnrollmentService;
import com.gestion.backend.services.StudentService;
import com.gestion.backend.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/inscriptions")
@RequiredArgsConstructor
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inscriptions", enrollmentService.getAllEnrollments());
        return "admin/inscriptions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiants", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "admin/inscriptions/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long studentId, @RequestParam Long courseId) {
        enrollmentService.enroll(studentId, courseId);
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return "redirect:/admin/inscriptions";
    }
}
