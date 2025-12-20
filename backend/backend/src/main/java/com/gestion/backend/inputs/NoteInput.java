package com.gestion.backend.inputs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteInput {
    private Double valeur;
    private String etudiantMatricule;
    private String coursCode;
}
