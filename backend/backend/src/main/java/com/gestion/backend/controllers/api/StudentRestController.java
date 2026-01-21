package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.StudentDto;
import com.gestion.backend.entities.Student;
import com.gestion.backend.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;
    private final com.gestion.backend.services.StudentGroupService groupService;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentService.findAll().stream()
                .map(StudentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(StudentDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        Student student = convertToEntity(studentDto);
        Student saved = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(StudentDto.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        Student student = convertToEntity(studentDto);
        Student updated = studentService.update(id, student);
        return ResponseEntity.ok(StudentDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Student convertToEntity(StudentDto dto) {
        Student student = new Student();
        student.setMatricule(dto.getMatricule());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        if (dto.getGroupId() != null) {
            student.setGroup(groupService.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found with ID: " + dto.getGroupId())));
        }
        return student;
    }
}
