package com.nucleus.floracestore.model.service;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CountryServiceModel {
    private Long countryId;
    private String countryCode;
    private String countryName;
}
