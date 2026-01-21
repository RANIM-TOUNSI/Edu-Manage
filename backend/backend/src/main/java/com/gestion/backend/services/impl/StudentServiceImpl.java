package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Student;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (studentRepository.findByMatricule(student.getMatricule()).isPresent()) {
            throw new RuntimeException("Matricule already exists");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student update(Long id, Student student) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        existing.setFirstName(student.getFirstName());
        existing.setLastName(student.getLastName());
        existing.setEmail(student.getEmail());
        if (student.getGroup() != null) {
            existing.setGroup(student.getGroup());
        }
        // Matricule and registration date typically shouldn't change
        return studentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByMatricule(String matricule) {
        return studentRepository.findByMatricule(matricule);
    }
}
