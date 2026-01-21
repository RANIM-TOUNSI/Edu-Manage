package com.gestion.backend.config;

import com.gestion.backend.entities.Specialty;
import com.gestion.backend.entities.Trainer;
import com.gestion.backend.services.SpecialtyService;
import com.gestion.backend.services.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TrainerService trainerService;
    private final SpecialtyService specialtyService;
    private final com.gestion.backend.services.CourseService courseService;
    private final com.gestion.backend.services.StudentGroupService studentGroupService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Trainer>() {
            @Override
            public Trainer convert(String id) {
                if (id == null || id.isEmpty() || id.equals("null")) {
                    return null;
                }
                return trainerService.findById(Long.valueOf(id)).orElse(null);
            }
        });

        registry.addConverter(new Converter<String, Specialty>() {
            @Override
            public Specialty convert(String id) {
                if (id == null || id.isEmpty() || id.equals("null")) {
                    return null;
                }
                return specialtyService.findById(Long.valueOf(id)).orElse(null);
            }
        });

        registry.addConverter(new Converter<String, com.gestion.backend.entities.Course>() {
            @Override
            public com.gestion.backend.entities.Course convert(String id) {
                if (id == null || id.isEmpty() || id.equals("null")) {
                    return null;
                }
                return courseService.findById(Long.valueOf(id)).orElse(null);
            }
        });

        registry.addConverter(new Converter<String, com.gestion.backend.entities.StudentGroup>() {
            @Override
            public com.gestion.backend.entities.StudentGroup convert(String id) {
                if (id == null || id.isEmpty() || id.equals("null")) {
                    return null;
                }
                return studentGroupService.findById(Long.valueOf(id)).orElse(null);
            }
        });
    }
}
