package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class AddressViewModel {
    private Long addressId;
    private String streetAddress;
    private String streetAddressSec;
    private String city;
    private String stateProvinceRegion;
    private String zipPostCode;
    private CountryViewModel country;
    private String otherAddressDetails;
    private AddressTypeViewModel addressType;
}
