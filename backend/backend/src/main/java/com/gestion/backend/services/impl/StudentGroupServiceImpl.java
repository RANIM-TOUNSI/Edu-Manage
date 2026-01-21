package com.gestion.backend.services.impl;

import com.gestion.backend.entities.StudentGroup;
import com.gestion.backend.repositories.StudentGroupRepository;
import com.gestion.backend.services.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentGroupServiceImpl implements StudentGroupService {
    private final StudentGroupRepository groupRepository;

    @Override
    public List<StudentGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public StudentGroup createGroup(StudentGroup group) {
        return groupRepository.save(group);
    }

    @Override
    public java.util.Optional<StudentGroup> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
