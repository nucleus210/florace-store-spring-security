package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.service.AddressServiceModel;

import java.util.Optional;

public interface AddressService {

    Optional<AddressEntity> getAddressById(Long addressId);

    void addAddress(AddressServiceModel addressServiceModel);

    void deleteAddress(Long id);

    void updateAddress(AddressServiceModel addressServiceModel);
}
