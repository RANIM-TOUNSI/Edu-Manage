package com.gestion.backend.controllers.api;

import com.gestion.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportRestController {

    private final ReportService reportService;

    @GetMapping("/student/{studentId}/pdf")
    public ResponseEntity<InputStreamResource> downloadGradesPdf(@PathVariable Long studentId) {
        ByteArrayInputStream bis = reportService.generateGradesPdf(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=grades.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
