package com.github.rdx7777.stripeinvoice.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerData {

    private String name;
    private String description; // not required
    private String phoneNumber; // not required
    private String email;
    private String city;
    private String postcode;
    private String street;
    private String houseNumber;
    private String apartmentNumber; // not required
    private String countryCode;
    private String taxId; // not required
}
