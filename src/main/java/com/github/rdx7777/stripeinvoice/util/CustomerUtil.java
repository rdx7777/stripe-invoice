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
import org.springframework.beans.factory.annotation.Value;

public class CustomerUtil {

    @Value("${STRIPE_PUBLIC_KEY}")
    private static String stripePublicKey;

    public static Customer createCustomer(CustomerData data) throws ServiceOperationException, StripeException {

        List<String> resultOfValidation = Validator.validateCustomerData(data);
        if (!resultOfValidation.isEmpty()) {
            throw new ServiceOperationException(String.format("Attempt to create customer with following invalid fields: %n%s", resultOfValidation));
        }

        Stripe.apiKey = stripePublicKey;

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

        return Customer.create(params);
    }

    public static CustomerCollection getAllCustomers() throws StripeException {

        Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);

        return Customer.list(params);
    }
}
