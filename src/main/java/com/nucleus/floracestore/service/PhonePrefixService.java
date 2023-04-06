package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.PhonePrefixServiceModel;

import java.util.List;

public interface PhonePrefixService {
    PhonePrefixServiceModel getPhonePrefixById(Long countryId);
    PhonePrefixServiceModel getPhonePrefixByCountryName(String countryName);
    PhonePrefixServiceModel getPhonePrefixByPrefix(String prefix);
    List<PhonePrefixServiceModel> getAllPhonePrefixes();
    PhonePrefixServiceModel createPhonePrefix(PhonePrefixServiceModel phonePrefixServiceModel);
    PhonePrefixServiceModel updatePhonePrefixById(PhonePrefixServiceModel phonePrefixServiceModel, Long phonePrefixId);
    PhonePrefixServiceModel deletePhonePrefixById(Long phonePrefixId);
}
