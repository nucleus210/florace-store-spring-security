package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ProductRatesServiceModel;

import java.util.List;

public interface ProductRatesService {
    ProductRatesServiceModel addProductRate(ProductRatesServiceModel model, String username);
    ProductRatesServiceModel getProductRateById(Long productRateId);
    ProductRatesServiceModel getProductRateByProductIdAndUsername(Long productId, String username);
    List<ProductRatesServiceModel> getAllProductRatesByProductId(Long productId);
    ProductRatesServiceModel rateProduct(ProductRatesServiceModel model, Long productId, String username);
}
