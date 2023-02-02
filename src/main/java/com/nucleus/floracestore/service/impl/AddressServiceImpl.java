package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.repository.AddressRepository;
import com.nucleus.floracestore.repository.ProfileRepository;
import com.nucleus.floracestore.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ProfileRepository profileRepository) {
        this.addressRepository = addressRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<AddressEntity> getAddressById(Long addressId) {
        return addressRepository.findByAddressId(addressId);
    }

    @Override
    public void addAddress(AddressServiceModel addressModel) {
        AddressEntity address = new AddressEntity();
        address.setStreetAddress(addressModel.getStreetAddress());
        address.setStreetAddressSec(addressModel.getStreetAddressSec());
        address.setCity(addressModel.getCity());
        address.setStateProvinceCounty(addressModel.getStateProvinceCounty());
        address.setZipPostCode(addressModel.getZipPostCode());
        address.setCountry(addressModel.getCountry());
        address.setOtherAddressDetails(addressModel.getOtherAddressDetails());
        address.setAddressTypeEnum(addressModel.getAddressTypeEnum());
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long profileId) {
        AddressEntity address = profileRepository.findById(profileId).get().getAddress();
        addressRepository.delete(address);
    }

    @Override
    public void updateAddress(AddressServiceModel addressModel) {
        AddressEntity address = new AddressEntity();
        address.setStreetAddress(addressModel.getStreetAddress());
        address.setStreetAddressSec(addressModel.getStreetAddressSec());
        address.setCity(addressModel.getCity());
        address.setStateProvinceCounty(addressModel.getStateProvinceCounty());
        address.setZipPostCode(addressModel.getZipPostCode());
        address.setCountry(addressModel.getCountry());
        address.setOtherAddressDetails(addressModel.getOtherAddressDetails());
        addressRepository.save(address);
    }
}
