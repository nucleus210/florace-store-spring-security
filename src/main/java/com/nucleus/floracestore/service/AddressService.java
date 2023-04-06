package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.AddressServiceModel;


public interface AddressService {

    AddressServiceModel getAddressById(Long addressId);


    AddressServiceModel createAddress(AddressServiceModel addressServiceModel);

    AddressServiceModel deleteAddress(Long id);

    AddressServiceModel updateAddress(AddressServiceModel addressServiceModel);

}
