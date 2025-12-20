package com.gestion.backend.services;

import com.gestion.backend.dtos.InscriptionDto;
import com.gestion.backend.inputs.InscriptionInput;
import java.util.List;

public interface InscriptionService {
    InscriptionDto createInscription(InscriptionInput input);

    InscriptionDto getInscriptionById(Long id); // Not standard but useful

    List<InscriptionDto> getAllInscriptions();

    void deleteInscription(Long id);
}
