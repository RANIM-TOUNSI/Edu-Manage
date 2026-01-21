package com.gestion.backend.services;

import com.gestion.backend.entities.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student save(Student student);

    Student update(Long id, Student student);

    void delete(Long id);

    Optional<Student> findById(Long id);

    List<Student> findAll();

    Optional<Student> findByEmail(String email);

    Optional<Student> findByMatricule(String matricule);
}
