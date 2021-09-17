package com.github.rdx7777.stripeinvoice.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceData {

    private String currency;
    private LocalDate dueDate;
    private List<InvoiceEntry> entries;
}
