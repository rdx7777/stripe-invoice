package com.github.rdx7777.stripeinvoice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.rdx7777.stripeinvoice.request.CustomerData;
import com.github.rdx7777.stripeinvoice.request.InvoiceData;
import com.github.rdx7777.stripeinvoice.request.InvoiceEntry;
import com.github.rdx7777.stripeinvoice.request.Vat;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import org.junit.jupiter.api.Test;

class InvoiceUtilTest {

    @Test
    void shouldCreateCustomerAndInvoice() throws StripeException, ServiceOperationException {
        // given
        CustomerData customerData = CustomerData.builder()
            .name("Test customer")
            .description("Customer created for test")
            .phoneNumber("0500500500")
            .email("radix_ok@o2.pl")
            .city("Warsaw")
            .postcode("00-001")
            .street("Test street")
            .houseNumber("7")
            .countryCode("PL")
            .taxId("800-001-01-01")
            .build();
        InvoiceEntry entry0 = InvoiceEntry.builder()
            .description("entry 0")
            .quantity(BigDecimal.valueOf(1))
            .price(BigDecimal.valueOf(100))
            .vatRate(Vat.VAT_0)
            .build();
        InvoiceEntry entry1 = InvoiceEntry.builder()
            .description("entry 1")
            .quantity(BigDecimal.valueOf(5))
            .price(BigDecimal.valueOf(200))
            .vatRate(Vat.VAT_5)
            .build();
        InvoiceEntry entry2 = InvoiceEntry.builder()
            .description("entry 2")
            .quantity(BigDecimal.valueOf(10))
            .price(BigDecimal.valueOf(50))
            .vatRate(Vat.VAT_23)
            .build();
        InvoiceData invoiceData = InvoiceData.builder()
            .currency("pln")
            .dueDate(LocalDate.now().plusDays(10))
            .entries(new ArrayList<>(Arrays.asList(entry0, entry1, entry2)))
            .build();

        // when
        Customer customer = CustomerUtil.createCustomer(customerData);
        Invoice invoice = InvoiceUtil.createInvoice(customer.getId(), invoiceData);

        // then
        assertEquals("Test customer", customer.getName());
        assertEquals("Warsaw", customer.getAddress().getCity());
        assertEquals(1765, invoice.getAmountDue());
    }
}
