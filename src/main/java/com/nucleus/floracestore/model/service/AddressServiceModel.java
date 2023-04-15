package com.nucleus.floracestore.model.service;

import lombok.Data;
@Data
public class AddressServiceModel {
    private Long addressId;
    private String streetAddress;
    private String streetAddressSec;
    private String city;
    private String stateProvinceRegion;
    private String zipPostCode;
    private CountryServiceModel country;
    private String otherAddressDetails;
    private AddressTypeServiceModel addressType;
}

