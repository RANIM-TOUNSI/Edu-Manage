package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.CoursDto;
import com.gestion.backend.services.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursRestController {

    private final CoursService coursService;

    @GetMapping
    public List<CoursDto> getAllCours() {
        return coursService.getAllCours();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CoursDto> getCours(@PathVariable String code) {
        return ResponseEntity.ok(coursService.getCoursByCode(code));
    }
}
