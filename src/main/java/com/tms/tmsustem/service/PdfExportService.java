package com.tms.tmsustem.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.tms.tmsustem.model.Task;
import jakarta.servlet.ServletOutputStream;

@Service
public class PdfExportService {

    public void exportTasksToPdf(List<Task> tasks, ServletOutputStream outputStream) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            for (Task task : tasks) {
                document.add(new Paragraph("Task ID: " + task.getId()));
                document.add(new Paragraph("Title: " + task.getTitle()));
                document.add(new Paragraph("Description: " + task.getDescription()));
                // Add more task details as needed
                document.add(new Paragraph("\n")); // Add spacing between tasks
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
