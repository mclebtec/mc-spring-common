package com.mclebtec.document.processor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteItem {

  private String description;
  private Integer quantity;
  private Double unitPrice;
  private Double total;

}
