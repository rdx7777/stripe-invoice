package com.github.rdx7777.stripeinvoice.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceData {

    private String number;
    private String currency;
    private LocalDate dueDate;
    private List<InvoiceEntry> entries;
}
