package com.mclebtec.document.processor.service;

import java.util.Map;

public interface DocumentService {

  void generateDocument(final String templateName, final Map<String, Object> data,
      final String pdfFileName);

}
