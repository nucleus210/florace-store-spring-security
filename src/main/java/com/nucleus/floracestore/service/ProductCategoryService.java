package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;

import java.util.List;
import java.util.Set;

public interface ProductCategoryService {
    ProductCategoryServiceModel getProductCategoryById(Long productCategoryId);

    ProductCategoryServiceModel getProductCategoryByCategoryName(String productCategoryName);

    List<ProductCategoryServiceModel> getAllProductCategories();

    ProductCategoryServiceModel createProductCategory(ProductCategoryServiceModel category, String username);

    ProductCategoryServiceModel updateProductCategoryById(Long productCategoryId, ProductCategoryServiceModel category, String username);

    ProductCategoryServiceModel deleteProductCategoryById(Long productCategoryId, String username);

    void saveAllCategories(Set<ProductCategoryEntity> categories);
}
