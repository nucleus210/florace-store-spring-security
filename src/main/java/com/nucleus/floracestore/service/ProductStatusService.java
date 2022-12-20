package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ProductStatusServiceModel;

import java.util.List;

public interface ProductStatusService {
    ProductStatusServiceModel createProductStatus(ProductStatusServiceModel productStatusServiceModel, String username);
    ProductStatusServiceModel updateProductStatusById(Long productStatusId, ProductStatusServiceModel productStatusServiceModel, String username);
    ProductStatusServiceModel deleteProductStatusById(Long productStatusId, String username);

    ProductStatusServiceModel getProductStatusById(Long productStatusId);
    ProductStatusServiceModel getProductStatusByProductStatusName(String productStatusName);
    List<ProductStatusServiceModel> getAllProductStatuses();
}
