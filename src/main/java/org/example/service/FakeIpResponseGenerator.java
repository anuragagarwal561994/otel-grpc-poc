package org.example.service;

import com.github.javafaker.Faker;
import org.example.ipservice.Ipservice;

public class FakeIpResponseGenerator {
    private final Faker faker = Faker.instance();

    public Ipservice.IPResponse generateResponse() {
        var responseBuilder = Ipservice.IPResponse.newBuilder();
        var address = faker.address();
        responseBuilder.setCountryCode(address.countryCode());
        responseBuilder.setLatitude(Double.parseDouble(address.latitude()));
        responseBuilder.setLongitude(Double.parseDouble(address.longitude()));
        responseBuilder.setCity(address.city());
        responseBuilder.setStateCode(address.stateAbbr());
        return responseBuilder.build();
    }
}
