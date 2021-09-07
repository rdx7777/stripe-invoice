package com.github.rdx7777.stripeinvoice.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InvoiceEntry {

    private String description;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private Vat vatRate;
}
