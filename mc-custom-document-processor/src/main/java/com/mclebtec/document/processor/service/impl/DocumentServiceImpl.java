package com.mclebtec.document.processor.service.impl;

import static com.mclebtec.document.processor.config.ThymeleafConfig.PATH;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.mclebtec.document.processor.service.DocumentService;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
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

      renderer.getFontResolver().addFont("%s/fonts/Code39.ttf".formatted(PATH),
          BaseFont.IDENTITY_H,
          BaseFont.EMBEDDED);

      ClassLoader classLoader = getClass().getClassLoader();
      URL resourceUrl = classLoader.getResource(PATH);

      if (Objects.isNull(resourceUrl)) {
        throw new IllegalArgumentException("Resource base URL not found!");
      }

      renderer.setDocumentFromString(htmlContent, resourceUrl.toString());

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
