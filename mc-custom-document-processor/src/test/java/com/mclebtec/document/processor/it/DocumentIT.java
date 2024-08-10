package com.mclebtec.document.processor.it;

import com.mclebtec.document.processor.DocumentProcessorConfig;
import com.mclebtec.document.processor.model.Customer;
import com.mclebtec.document.processor.model.QuoteItem;
import com.mclebtec.document.processor.service.DocumentService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Import(DocumentProcessorConfig.class)
@ContextConfiguration(classes = {DocumentProcessorConfig.class})
class DocumentIT {

  @Autowired
  private DocumentService documentService;

  @Test
  void generateDocument_SampleQuotationDocument_Success() {
    // GIVEN
    Map<String, Object> data = new HashMap<>();

    Customer customer = Customer.builder()
        .companyName("Simple Solution")
        .contactName("John Doe")
        .address("123, Simple Street")
        .email("john@simplesolution.dev")
        .phone("123 456 789")
        .build();

    data.put("customer", customer);

    List<QuoteItem> quoteItems = new ArrayList<>();
    QuoteItem quoteItem1 = new QuoteItem();
    quoteItem1.setDescription("Test Quote Item 1");
    quoteItem1.setQuantity(1);
    quoteItem1.setUnitPrice(100.0);
    quoteItem1.setTotal(100.0);
    quoteItems.add(quoteItem1);

    QuoteItem quoteItem2 = new QuoteItem();
    quoteItem2.setDescription("Test Quote Item 2");
    quoteItem2.setQuantity(4);
    quoteItem2.setUnitPrice(500.0);
    quoteItem2.setTotal(2000.0);
    quoteItems.add(quoteItem2);

    QuoteItem quoteItem3 = new QuoteItem();
    quoteItem3.setDescription("Test Quote Item 3");
    quoteItem3.setQuantity(2);
    quoteItem3.setUnitPrice(200.0);
    quoteItem3.setTotal(400.0);
    quoteItems.add(quoteItem3);

    data.put("quoteItems", quoteItems);

    // WHEN
    documentService.generateDocument("quotation", data, "quotation.pdf");
  }

}
