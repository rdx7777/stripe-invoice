package com.github.rdx7777.stripeinvoice.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.rdx7777.stripeinvoice.request.CustomerData;
import com.github.rdx7777.stripeinvoice.request.InvoiceData;
import com.github.rdx7777.stripeinvoice.request.InvoiceEntry;
import com.github.rdx7777.stripeinvoice.request.Vat;

public class Validator {

    public static List<String> validateCustomerData(CustomerData data) {
        if (data == null) {
            return Collections.singletonList("Customer data cannot be null.");
        }

        List<String> result = new ArrayList<>();

        result.add(validateName(data.getName()));
        result.add(validateEmail(data.getEmail()));
        result.add(validateCity(data.getCity()));
        result.add(validatePostcode(data.getPostcode()));
        result.add(validateStreet(data.getStreet()));
        result.add(validateHouseNumber(data.getHouseNumber()));
        result.add(validateCountryCode(data.getCountryCode()));

        result = result.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }

    private static String validateName(String name) {
        if (name == null) {
            return "Name cannot be null.";
        }
        if (name.trim().isEmpty()) {
            return "Name must contain at least 1 character.";
        }
        return null;
    }

    private static String validateEmail(String email) {
        if (email == null) {
            return "Email address cannot be null.";
        }
        if (!RegexPatterns.emailPatternCheck(email)) {
            return "Email address does not match correct email pattern.";
        }
        return null;
    }

    private static String validateCity(String city) {
        if (city == null) {
            return "City cannot be null.";
        }
        if (city.trim().isEmpty()) {
            return "City must contain at least 1 character.";
        }
        return null;
    }

    private static String validatePostcode(String postcode) {
        if (postcode == null) {
            return "Postcode cannot be null.";
        }
        if (postcode.trim().isEmpty()) {
            return "Postcode must contain at least 1 character.";
        }
        return null;
    }

    private static String validateStreet(String street) {
        if (street == null) {
            return "Street cannot be null.";
        }
        if (street.trim().isEmpty()) {
            return "Street must contain at least 1 character.";
        }
        return null;
    }

    private static String validateHouseNumber(String houseNumber) {
        if (houseNumber == null) {
            return "House number cannot be null.";
        }
        if (houseNumber.trim().isEmpty()) {
            return "House number must contain at least 1 character.";
        }
        return null;
    }

    private static String validateCountryCode(String countryCode) {
        if (countryCode == null) {
            return "Country code cannot be null.";
        }
        List<String> countries = Arrays.asList(Locale.getISOCountries());
        if (!countries.contains(countryCode)) {
            return "Country code is not from an approved code collection.";
        }
        return null;
    }

    public static List<String> validateInvoiceData(InvoiceData data) {
        if (data == null) {
            return Collections.singletonList("Invoice data cannot be null.");
        }

        List<String> result = new ArrayList<>();

        result.add(validateCurrency(data.getCurrency()));
        result.add(validateDueDate(data.getDueDate()));

        for (InvoiceEntry entry : data.getEntries()) {
            result.addAll(validateInvoiceEntry(entry));
        }

        result = result.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }

    private static String validateCurrency(String currency) {
        if (currency == null) {
            return "Currency cannot be null.";
        }
        currency = currency.toLowerCase();
        if (!currency.equals("pln") && !currency.equals("eur")) {
            return "Currency outside the supported range.";
        }
        return null;
    }

    private static String validateDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            return "Due date cannot be null.";
        }
        if (dueDate.isBefore(LocalDate.now())) {
            return "Due date cannot refer to the past.";
        }
        return null;
    }

    private static List<String> validateInvoiceEntry(InvoiceEntry entry) {

        List<String> result = new ArrayList<>();

        result.add(validateDescription(entry.getDescription()));
        result.add(validateQuantity(entry.getQuantity()));
        result.add(validatePrice(entry.getPrice()));
        result.add(validateVatRate(entry.getVatRate()));

        result = result.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }

    private static String validateDescription(String description) {
        if (description == null) {
            return "Description cannot be null.";
        }
        if (description.trim().isEmpty()) {
            return "Description must contain at least 1 character.";
        }
        return null;
    }

    private static String validateQuantity(BigDecimal quantity) {
        if (quantity == null) {
            return "Quantity cannot be null.";
        }
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            return "Quantity cannot be lower than zero.";
        }
        return null;
    }

    private static String validatePrice(BigDecimal price) {
        if (price == null) {
            return "Price cannot be null.";
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            return "Price cannot be lower than zero.";
        }
        return null;
    }

    private static String validateVatRate(Vat vatRate) {
        if (vatRate == null) {
            return "Vat rate cannot be null.";
        }
        return null;
    }
}
