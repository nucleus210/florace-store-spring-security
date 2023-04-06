package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.CountryServiceModel;

import java.util.List;

public interface CountryService {
    CountryServiceModel getCountryById(Long countryId);
    CountryServiceModel getCountryByCountryName(String countryName);
    CountryServiceModel getCountryByCountryCode(String countryCode);
    List<CountryServiceModel> getAllCountries();
    CountryServiceModel createCountry(CountryServiceModel countryServiceModel);
    CountryServiceModel updateAnswer(CountryServiceModel countryServiceModel, Long countryId);
    CountryServiceModel deleteCountryById(Long countryId);
}
