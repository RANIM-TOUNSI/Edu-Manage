package com.gestion.backend.controllers.admin;

import com.gestion.backend.services.GradeService;
import com.gestion.backend.services.StudentService;
import com.gestion.backend.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/notes")
@RequiredArgsConstructor
public class AdminGradeController {

    private final GradeService gradeService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notes", gradeService.getAllGrades());
        return "admin/notes/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiants", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "admin/notes/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long studentId, @RequestParam Long courseId, @RequestParam Double value) {
        gradeService.assignGrade(studentId, courseId, value);
        return "redirect:/admin/notes";
    }
}
