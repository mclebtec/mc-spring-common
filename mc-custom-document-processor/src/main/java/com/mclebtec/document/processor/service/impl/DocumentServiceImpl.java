package com.mclebtec.document.processor.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.mclebtec.document.processor.service.DocumentService;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

  @Value("${document.output}")
  private String documentOutput;

  private final TemplateEngine templateEngine;

  @Override
  public void generateDocument(final String templateName, final Map<String, Object> data,
      final String pdfFileName) {

    Context context = new Context();

    context.setVariables(data);

    String htmlContent = templateEngine.process(templateName, context);

    try {

      ITextRenderer renderer = new ITextRenderer();

      renderer.getFontResolver().addFont("Code39.ttf",
          BaseFont.IDENTITY_H,
          BaseFont.EMBEDDED);

      String baseUrl = FileSystems
          .getDefault()
          .getPath("src", "main", "resources")
          .toUri()
          .toURL()
          .toString();

      renderer.setDocumentFromString(htmlContent, baseUrl);

      renderer.layout();

      Path directoryPath = Paths.get(documentOutput);
      if (!Files.exists(directoryPath)) {
        Files.createDirectories(directoryPath);
      }

      // create file
      Path filePath = Paths.get(documentOutput + pdfFileName);
      try (OutputStream fileOutputStream = Files.newOutputStream(filePath,
          StandardOpenOption.CREATE)) {

        renderer.createPDF(fileOutputStream, false);

        renderer.finishPDF();
      }

    } catch (DocumentException | IOException e) {

      log.error("Error creating document with, {}", e.getMessage(), e);

    }
  }
}
