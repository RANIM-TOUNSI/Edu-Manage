package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Grade;
import com.gestion.backend.entities.Student;
import com.gestion.backend.repositories.GradeRepository;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.services.ReportService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    @Override
    public double calculateStudentGpa(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty())
            return 0.0;
        return grades.stream().mapToDouble(Grade::getValue).average().orElse(0.0);
    }

    @Override
    public double calculateCourseSuccessRate(Long courseId) {
        List<Grade> grades = gradeRepository.findByCourseId(courseId);
        if (grades.isEmpty())
            return 0.0;
        long successful = grades.stream().filter(g -> g.getValue() >= 10).count();
        return (double) successful / grades.size() * 100;
    }

    @Override
    public ByteArrayInputStream generateGradesPdf(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Relevé de Notes - " + student.getFirstName() + " " + student.getLastName()));
            document.add(new Paragraph("Matricule: " + student.getMatricule()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = { 200f, 100f };
            Table table = new Table(columnWidths);
            table.addCell("Cours");
            table.addCell("Note");

            for (Grade grade : grades) {
                table.addCell(grade.getCourse().getTitle());
                table.addCell(String.valueOf(grade.getValue()));
            }

            document.add(table);
            document.add(new Paragraph("\nMoyenne Générale: " + String.format("%.2f", calculateStudentGpa(studentId))));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
