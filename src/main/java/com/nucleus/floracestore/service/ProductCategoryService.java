package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.view.ProductCategoryViewModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductCategoryService {
    Optional<ProductCategoryEntity> getByProductCategoryName(String productCategoryName);

    List<ProductCategoryViewModel> getAll();

    List<ProductCategoryEntity> getAllEntity();

    ProductCategoryEntity getById(Long id);

    List<String> getAllCategories();

    void saveAll(Set<ProductCategoryEntity> categories);

    void initializeCategories();

}
