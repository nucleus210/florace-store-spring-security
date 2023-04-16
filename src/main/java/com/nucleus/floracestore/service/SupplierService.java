package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.SupplierServiceModel;

import java.util.List;

public interface SupplierService {

    SupplierServiceModel createSupplier(SupplierServiceModel supplierServiceModel);

    SupplierServiceModel getSupplierById(Long supplierId);

    SupplierServiceModel updateSupplier(SupplierServiceModel supplierServiceModel, Long supplierId);

    SupplierServiceModel deleteSupplierById(Long supplierId);
    List<SupplierServiceModel> getAllSuppliers();

}
