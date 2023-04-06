package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.CountryEntity;
import com.nucleus.floracestore.model.service.CountryServiceModel;
import com.nucleus.floracestore.repository.CountryRepository;
import com.nucleus.floracestore.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(ModelMapper modelMapper, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryServiceModel getCountryById(Long countryId) {
        log.info("Country service: " + countryId);
        return countryRepository.findById(countryId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find answer with id " + countryId));
    }
    @Override
    public CountryServiceModel getCountryByCountryName(String countryName) {
        CountryEntity country  = countryRepository.findCountryEntityByCountryName(countryName)
                .orElseThrow(() -> new QueryRuntimeException("Could not find country with country name " + countryName));
        return mapToService(country);
    }

    @Override
    public CountryServiceModel getCountryByCountryCode(String countryCode) {
        CountryEntity country  = countryRepository.findCountryEntityByCountryCode(countryCode)
                .orElseThrow(() -> new QueryRuntimeException("Could not find country with country code " + countryCode));
        return mapToService(country);
    }
    @Override
    public List<CountryServiceModel> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public CountryServiceModel createCountry(CountryServiceModel countryServiceModel) {
        CountryEntity country = modelMapper.map(countryServiceModel, CountryEntity.class);
        return mapToService(countryRepository.save(country));
    }

    @Override
    public CountryServiceModel updateAnswer(CountryServiceModel countryServiceModel, Long countryId) {
        CountryEntity country  = countryRepository.findById(countryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find country with Id " + countryId));
        country.setCountryCode(countryServiceModel.getCountryCode());
        country.setCountryName(countryServiceModel.getCountryName());

        return mapToService(countryRepository.save(country));
    }

    @Override
    public CountryServiceModel deleteCountryById(Long countryId) {
        CountryEntity country  = countryRepository.findById(countryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find country with Id " + countryId));
        countryRepository.delete(country);
        return mapToService(country);
    }

    private CountryServiceModel mapToService(CountryEntity countryEntity) {
        return this.modelMapper.map(countryEntity, CountryServiceModel.class);
    }
}
