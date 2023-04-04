package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import lombok.Data;

@Data
public class AddressViewModel {
    private Long addressId;
    private String streetAddress;
    private String streetAddressSec;
    private String city;
    private String stateProvinceRegion;
    private String zipPostCode;
    private String country;
    private String otherAddressDetails;
    private AddressTypeEnum addressType;
}
