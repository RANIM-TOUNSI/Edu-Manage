package com.gestion.backend.dtos;

import com.gestion.backend.entities.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;

    @NotBlank(message = "Matricule is required")
    private String matricule;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private LocalDate registrationDate;
    private Long groupId;
    private String groupName;

    public static StudentDto fromEntity(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setMatricule(student.getMatricule());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setRegistrationDate(student.getRegistrationDate());
        if (student.getGroup() != null) {
            dto.setGroupId(student.getGroup().getId());
            dto.setGroupName(student.getGroup().getName());
        }
        return dto;
    }
}
