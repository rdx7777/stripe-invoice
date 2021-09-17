package com.github.rdx7777.stripeinvoice.util;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

import com.github.rdx7777.stripeinvoice.request.InvoiceData;
import com.github.rdx7777.stripeinvoice.request.InvoiceEntry;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceUtil {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceUtil.class);

    /**
     * Creates a new {@link Invoice Stripe's invoice} on the basis of passed {@link InvoiceData} object.
     *
     * @param customerId {@link String}; existing customer id, stored in Stripe account.
     * @param data {@link InvoiceData}.
     * @return {@link Invoice an invoice} recently created and saved in Stripe account. The invoice is finalized and ready to get, so no more changes are available.
     * @throws ServiceOperationException when something went wrong during customerId or {@link InvoiceData} validation.
     * @throws StripeException when something went wrong on Stripe's servers.
     */
    public static Invoice createInvoice(String customerId, InvoiceData data) throws ServiceOperationException, StripeException {

        if (customerId == null) {
            logger.error("Attempt to create an invoice with null customer.");
            throw new ServiceOperationException("Customer id cannot be null.");
        }
        List<String> resultOfValidation = Validator.validateInvoiceData(data);
        if (!resultOfValidation.isEmpty()) {
            logger.error(String.format("Attempt to create an invoice with following invalid fields: %n%s", resultOfValidation));
            throw new ServiceOperationException(String.format("Attempt to create invoice with following invalid fields: %n%s", resultOfValidation));
        }

        Stripe.apiKey = System.getenv("STRIPE_PUBLIC_KEY");

        for (InvoiceEntry entry : data.getEntries()) {
            BigDecimal netAmount = entry.getPrice().multiply(entry.getQuantity());
            BigDecimal vatAmount = netAmount.multiply(BigDecimal.valueOf(entry.getVatRate().getValue()));
            BigDecimal grossAmount = netAmount.add(vatAmount);
            InvoiceItemCreateParams invoiceItemParams = InvoiceItemCreateParams.builder()
                .setCustomer(customerId)
                .setDescription(entry.getDescription())
                .setAmount(grossAmount.longValue())
                .setCurrency(data.getCurrency())
                .build();
            InvoiceItem.create(invoiceItemParams);
        }

        InvoiceCreateParams invoiceParams = InvoiceCreateParams.builder()
            .setCustomer(customerId)
            .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
            .setDueDate(data.getDueDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond())
            .setAutoAdvance(false)
            .build();

        Invoice invoice = Invoice.create(invoiceParams).finalizeInvoice();
        logger.info(String.format("Invoice created, id: %s", invoice.getId()));
        return invoice;
    }

    /**
     * Returns {@link Invoice an invoice} stored in Stripe's account on the basis of passed {@link String} invoice id.
     *
     * @param id {@link String}.
     * @return {@link Invoice an invoice} retrieved from Stripe account.
     * @throws ServiceOperationException when something went wrong during invoice id validation.
     * @throws StripeException when something went wrong on Stripe's servers.
     */
    public static Invoice getInvoice(String id) throws ServiceOperationException, StripeException {
        if (id == null) {
            logger.error("Attempt to get an invoice with null id.");
            throw new ServiceOperationException("Invoice id cannot be null.");
        }
        Stripe.apiKey = System.getenv("STRIPE_PUBLIC_KEY");
        Invoice invoice = Invoice.retrieve(id);
        logger.info(String.format("Invoice retrieved, id: %s", invoice.getId()));
        return invoice;
    }
}
