package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.CountryEntity;
import com.nucleus.floracestore.model.entity.PhonePrefixEntity;
import com.nucleus.floracestore.model.service.PhonePrefixServiceModel;
import com.nucleus.floracestore.repository.PhonePrefixRepository;
import com.nucleus.floracestore.service.PhonePrefixService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhonePrefixServiceImpl implements PhonePrefixService {

    private final ModelMapper modelMapper;
    private final PhonePrefixRepository phonePrefixRepository;

    @Autowired
    public PhonePrefixServiceImpl(ModelMapper modelMapper, PhonePrefixRepository phonePrefixRepository) {
        this.modelMapper = modelMapper;
        this.phonePrefixRepository = phonePrefixRepository;
    }

    @Override
    public PhonePrefixServiceModel getPhonePrefixById(Long phonePrefixId) {

        return phonePrefixRepository.findById(phonePrefixId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find phone prefix with id " + phonePrefixId));
    }

    @Override
    public PhonePrefixServiceModel getPhonePrefixByCountryName(String countryName) {
        return phonePrefixRepository.findPhonePrefixEntityByCountryName(countryName).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find phone prefix with country name " + countryName));
    }

    @Override
    public PhonePrefixServiceModel getPhonePrefixByPrefix(String prefix) {
        return phonePrefixRepository.findPhonePrefixEntityByCountryName(prefix).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find phone prefix with prefix " + prefix));

    }

    @Override
    public List<PhonePrefixServiceModel> getAllPhonePrefixes() {
        return phonePrefixRepository.findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public PhonePrefixServiceModel createPhonePrefix(PhonePrefixServiceModel phonePrefixServiceModel) {
        PhonePrefixEntity phonePrefix = modelMapper.map(phonePrefixServiceModel, PhonePrefixEntity.class);
        return mapToService(phonePrefixRepository.save(phonePrefix));
    }


    @Override
    public PhonePrefixServiceModel updatePhonePrefixById(PhonePrefixServiceModel phonePrefixServiceModel, Long phonePrefixId) {
        PhonePrefixEntity phonePrefix  = phonePrefixRepository.findById(phonePrefixId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find phone prefix with id " + phonePrefixId));
        phonePrefix.setCountryName(phonePrefixServiceModel.getCountryName());
        phonePrefix.setPrefix(phonePrefixServiceModel.getPrefix());

        return mapToService(phonePrefixRepository.save(phonePrefix));    }

    @Override
    public PhonePrefixServiceModel deletePhonePrefixById(Long phonePrefixId) {
        PhonePrefixEntity phonePrefix  = phonePrefixRepository.findById(phonePrefixId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find phone prefix with id " + phonePrefixId));
        phonePrefixRepository.delete(phonePrefix);
        return mapToService(phonePrefix);
    }
    private PhonePrefixServiceModel mapToService(PhonePrefixEntity phonePrefix) {
        return modelMapper.map(phonePrefix, PhonePrefixServiceModel.class);
    }

}
