package com.github.rdx7777.stripeinvoice.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.rdx7777.stripeinvoice.request.CustomerData;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.param.CustomerCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerUtil {

    private static final Logger logger = LoggerFactory.getLogger(CustomerUtil.class);

    public static Customer createCustomer(CustomerData data) throws ServiceOperationException, StripeException {

        List<String> resultOfValidation = Validator.validateCustomerData(data);
        if (!resultOfValidation.isEmpty()) {
            logger.error(String.format("Attempt to create a customer with following invalid fields: %n%s", resultOfValidation));
            throw new ServiceOperationException(String.format("Attempt to create customer with following invalid fields: %n%s", resultOfValidation));
        }

        Stripe.apiKey = System.getenv("STRIPE_PUBLIC_KEY");

        String streetAndHouseNumber = data.getStreet() + " " + data.getHouseNumber();
        String apartmentNumber = "";
        if (data.getApartmentNumber() != null) {
            apartmentNumber = "apartment number: " + data.getApartmentNumber();
        }

        CustomerCreateParams params = CustomerCreateParams.builder()
            .setName(data.getName())
            .setDescription(data.getDescription())
            .setPhone(data.getPhoneNumber())
            .setEmail(data.getEmail())
            .setAddress(CustomerCreateParams.Address.builder()
                .setCity(data.getCity())
                .setPostalCode(data.getPostcode())
                .setLine1(streetAndHouseNumber)
                .setLine2(apartmentNumber)
                .setCountry(data.getCountryCode())
                .build())
            .build();

        Customer customer = Customer.create(params);
        logger.info(String.format("Customer created, id: %s", customer.getId()));
        return customer;
    }

    public static CustomerCollection getAllCustomers() throws StripeException {

        Stripe.apiKey = System.getenv("STRIPE_PUBLIC_KEY");

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);

        logger.info("Attempt to get all customers.");
        return Customer.list(params);
    }
}
