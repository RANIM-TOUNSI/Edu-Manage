package com.gestion.backend.services;

import com.gestion.backend.entities.StudentGroup;
import java.util.List;

public interface StudentGroupService {
    List<StudentGroup> getAllGroups();

    StudentGroup createGroup(StudentGroup group);

    java.util.Optional<StudentGroup> findById(Long id);

    void deleteGroup(Long id);
}
