package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.AddressTypeEntity;
import com.nucleus.floracestore.model.service.AddressTypeServiceModel;
import com.nucleus.floracestore.repository.AddressTypeRepository;
import com.nucleus.floracestore.service.AddressTypeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressTypeServiceImpl implements AddressTypeService {

    private final ModelMapper modelMapper;
    private final AddressTypeRepository addressTypeRepository;

    @Autowired
    public AddressTypeServiceImpl(ModelMapper modelMapper, AddressTypeRepository addressTypeRepository) {
        this.modelMapper = modelMapper;
        this.addressTypeRepository = addressTypeRepository;
    }

    @Override
    public AddressTypeServiceModel getAddressTypeById(Long addressTypeId) {
        return addressTypeRepository.findById(addressTypeId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find address type with id " + addressTypeId));
    }

    @Override
    public AddressTypeServiceModel getAddressTypeByName(String addressTypeName) {
        return addressTypeRepository.findAddressTypeByAddressTypeName(addressTypeName).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find address type with name " + addressTypeName));
    }

    @Override
    public List<AddressTypeServiceModel> getAllAddressTypes() {
        return addressTypeRepository.findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public AddressTypeServiceModel createAddressType(AddressTypeServiceModel addressTypeServiceModel) {
        AddressTypeEntity addressType = modelMapper.map(addressTypeServiceModel, AddressTypeEntity.class);
        return mapToService(addressTypeRepository.save(addressType));
    }

    @Override
    public AddressTypeServiceModel updateAddressTypeById(AddressTypeServiceModel addressTypeServiceModel, Long addressTypeId) {
        AddressTypeEntity addressType  = addressTypeRepository.findById(addressTypeId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find address type with id " + addressTypeId));
        addressType.setAddressTypeName(addressTypeServiceModel.getAddressTypeName());
        addressType.setAddressTypeDescription(addressTypeServiceModel.getAddressTypeDescription());
        return mapToService(addressTypeRepository.save(addressType));
    }

    @Override
    public AddressTypeServiceModel deleteAddressTypeById(Long addressTypeId) {
        AddressTypeEntity addressType  = addressTypeRepository.findById(addressTypeId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find address type with id " + addressTypeId));
        addressTypeRepository.delete(addressType);
        return mapToService(addressType);
    }

    private AddressTypeServiceModel mapToService(AddressTypeEntity addressTypeEntity) {
        return modelMapper.map(addressTypeEntity, AddressTypeServiceModel.class);
    }
}
