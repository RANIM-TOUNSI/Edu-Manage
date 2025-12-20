package com.gestion.backend.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class InscriptionInput {

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @NotBlank(message = "L'étudiant est obligatoire")
    private String etudiantMatricule;

    @NotBlank(message = "Le cour est obligatoire")
    private String coursCode;
}
