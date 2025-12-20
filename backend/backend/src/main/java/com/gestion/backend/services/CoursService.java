package com.gestion.backend.services;

import com.gestion.backend.dtos.CoursDto;
import com.gestion.backend.inputs.CoursInput;
import java.util.List;

public interface CoursService {
    CoursDto createCours(CoursInput input);

    CoursDto updateCours(String code, CoursInput input);

    CoursDto getCoursByCode(String code);

    List<CoursDto> getAllCours();

    void deleteCours(String code);
}
