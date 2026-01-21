package com.gestion.backend.controllers.admin;

import com.gestion.backend.entities.Trainer;
import com.gestion.backend.entities.Specialty;
import com.gestion.backend.entities.Course;
import com.gestion.backend.services.CourseService;
import com.gestion.backend.services.TrainerService;
import com.gestion.backend.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cours")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;
    private final TrainerService trainerService;
    private final SpecialtyService specialtyService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin/cours/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Course course = new Course();
        course.setTrainer(new Trainer());
        course.setSpecialty(new Specialty());
        model.addAttribute("courseInput", course);
        model.addAttribute("trainers", trainerService.findAll());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("isEdit", false);
        return "admin/cours/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("courseInput") Course course) {
        courseService.save(course);
        return "redirect:/admin/cours";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
        if (course.getTrainer() == null)
            course.setTrainer(new Trainer());
        if (course.getSpecialty() == null)
            course.setSpecialty(new Specialty());
        model.addAttribute("courseInput", course);
        model.addAttribute("trainers", trainerService.findAll());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("isEdit", true);
        return "admin/cours/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("courseInput") Course course) {
        courseService.update(id, course);
        return "redirect:/admin/cours";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/admin/cours";
    }
}
