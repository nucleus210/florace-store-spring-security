package com.nucleus.floracestore.model.service;


import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import lombok.Data;

@Data
public class AddressServiceModel {
    private Long addressId;
    private String streetAddress;
    private String streetAddressSec;
    private String city;
    private String stateProvinceCounty;
    private String zipPostCode;
    private String country;
    private String otherAddressDetails;
    private AddressTypeEnum addressTypeEnum;
}

