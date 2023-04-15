package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.entity.Supplier;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.service.SupplierServiceModel;
import com.nucleus.floracestore.repository.SupplierRepository;
import com.nucleus.floracestore.service.AddressService;
import com.nucleus.floracestore.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(ModelMapper modelMapper, AddressService addressService, SupplierRepository supplierRepository) {
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.supplierRepository = supplierRepository;
    }


    @Override
    public SupplierServiceModel createSupplier(SupplierServiceModel supplierServiceModel) {
        AddressServiceModel address = addressService.createAddress(supplierServiceModel.getAddress());
        supplierServiceModel.setAddress(address);
        Supplier supplier = supplierRepository.save(modelMapper.map(supplierServiceModel, Supplier.class));
        return mapToService(supplier);
    }



    @Override
    public SupplierServiceModel getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find supplier with id: " + supplierId));
        return mapToService(supplier);
    }

    @Override
    public SupplierServiceModel updateSupplier(SupplierServiceModel supplierServiceModel, Long supplierId) {
        return null;
    }

    @Override
    public SupplierServiceModel deleteSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find supplier with id: " + supplierId));
        supplierRepository.delete(supplier);
        return mapToService(supplier);
    }

    @Override
    public List<SupplierServiceModel> getAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::mapToService).collect(Collectors.toList());
    }

    private SupplierServiceModel mapToService(Supplier supplier) {
        return modelMapper.map(supplier, SupplierServiceModel.class);
    }
}
