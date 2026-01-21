package com.gestion.backend.controllers.admin;

import com.gestion.backend.entities.Student;
import com.gestion.backend.entities.StudentGroup;
import com.gestion.backend.services.StudentGroupService;
import com.gestion.backend.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/etudiants")
@RequiredArgsConstructor
public class AdminEtudiantController {

    private final StudentService studentService;
    private final StudentGroupService groupService;

    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", studentService.findAll());
        return "admin/etudiants/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Student student = new Student();
        student.setGroup(new StudentGroup());
        model.addAttribute("etudiantInput", student);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("isEdit", false);
        return "admin/etudiants/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("etudiantInput") Student student) {
        studentService.save(student);
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Student with ID " + id + " not found"));
        if (student.getGroup() == null)
            student.setGroup(new StudentGroup());
        model.addAttribute("etudiantInput", student);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("isEdit", true);
        return "admin/etudiants/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("etudiantInput") Student student) {
        studentService.update(id, student);
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/admin/etudiants";
    }
}
