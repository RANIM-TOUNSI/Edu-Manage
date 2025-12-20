package com.gestion.backend.services;

import com.gestion.backend.dtos.FormateurDto;
import com.gestion.backend.inputs.FormateurInput;
import java.util.List;

public interface FormateurService {
    FormateurDto createFormateur(FormateurInput input);

    FormateurDto updateFormateur(Long id, FormateurInput input);

    FormateurDto getFormateurById(Long id);

    List<FormateurDto> getAllFormateurs();

    void deleteFormateur(Long id);
}
