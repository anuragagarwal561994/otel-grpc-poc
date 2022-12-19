package org.example.client;

import lombok.Value;

@Value
class IPServiceDto {
    String asn;
    String stateCode;
    String countryCode;
    Double latitude;
    Double longitude;
    String city;
    String dma;
}
