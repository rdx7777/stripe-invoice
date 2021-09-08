package com.github.rdx7777.stripeinvoice.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InvoiceEntry {

    private String description;
    private BigDecimal quantity;
    private BigDecimal price;
    private Vat vatRate;
}
