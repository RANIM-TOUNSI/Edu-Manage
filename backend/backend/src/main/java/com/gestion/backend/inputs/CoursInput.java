package com.gestion.backend.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CoursInput {

    @NotBlank(message = "Le code du cours est obligatoire")
    private String code;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "Le formateur est obligatoire")
    private Long formateurId;
}
