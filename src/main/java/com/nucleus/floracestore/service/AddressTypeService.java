package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.AddressTypeServiceModel;

import java.util.List;

public interface AddressTypeService {
    AddressTypeServiceModel getAddressTypeById(Long addressTypeId);
    AddressTypeServiceModel getAddressTypeByName(String addressTypeName);
    List<AddressTypeServiceModel> getAllAddressTypes();
    AddressTypeServiceModel createAddressType(AddressTypeServiceModel addressTypeServiceModel);
    AddressTypeServiceModel updateAddressTypeById(AddressTypeServiceModel addressTypeServiceModel, Long addressTypeId);
    AddressTypeServiceModel deleteAddressTypeById(Long addressTypeId);
}
