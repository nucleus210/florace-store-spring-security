package com.nucleus.floracestore.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddressDto {
    private Long addressId;
    @NotNull
    @Size(min = 10, max = 200)
    private String streetAddress;
    @Size(min = 10, max = 200)
    private String streetAddressSec;
    @NotNull
    @Size(min = 4, max = 30)
    private String city;
    @NotNull
    @Size(min = 4, max = 30)
    private String stateProvinceRegion;
    @NotNull
    @Size(min = 4, max = 30)
    private String zipPostCode;
    @NotNull
    private String country;
    @NotNull
    @Size(min = 10, max = 200)
    private String otherAddressDetails;
    @NotNull
    private String addressType;
}
