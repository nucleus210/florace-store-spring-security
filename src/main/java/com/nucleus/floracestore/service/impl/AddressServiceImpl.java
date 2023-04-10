package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.repository.AddressRepository;
import com.nucleus.floracestore.repository.ProfileRepository;
import com.nucleus.floracestore.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public AddressServiceImpl(ModelMapper modelMapper,
                              AddressRepository addressRepository,
                              ProfileRepository profileRepository) {
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public AddressServiceModel getAddressById(Long addressId) {
        AddressEntity address = addressRepository.findById(addressId).orElseThrow(()->
                new QueryRuntimeException("Could not find address with id " + addressId));
        return mapToService(address);
    }

    @Override
    public AddressServiceModel createAddress(AddressServiceModel addressModel) {
        AddressEntity address = modelMapper.map(addressModel, AddressEntity.class);
        addressRepository.save(address);
        return mapToService(addressRepository.save(address));
    }

    @Override
    public AddressServiceModel deleteAddress(Long addressId) {
        AddressEntity address = addressRepository.findById(addressId).orElseThrow(()->
                new QueryRuntimeException("Could not find address with id " + addressId));
        addressRepository.delete(address);
        return mapToService(address);
    }

    @Override
    public AddressServiceModel updateAddress(AddressServiceModel addressModel) {
        AddressEntity address = new AddressEntity();
        address.setStreetAddress(addressModel.getStreetAddress());
        address.setStreetAddressSec(addressModel.getStreetAddressSec());
        address.setCity(addressModel.getCity());
        address.setStateProvinceRegion(addressModel.getStateProvinceRegion());
        address.setZipPostCode(addressModel.getZipPostCode());
//        address.setCountry(addressModel.getCountry());
        address.setOtherAddressDetails(addressModel.getOtherAddressDetails());

        return mapToService(addressRepository.save(address));
    }

    private AddressServiceModel mapToService(AddressEntity address) {
        return modelMapper.map(address, AddressServiceModel.class);
    }
}
