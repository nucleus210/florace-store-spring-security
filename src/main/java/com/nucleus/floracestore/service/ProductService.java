package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductEntity> getAllProducts();

    ProductViewModel getByIdAndCurrentUser(Long id, String owner);
    ProductViewModel getById(Long id);

    Optional<ProductEntity> getProductById(Long projectId);

    Optional<ProductEntity> getByProductName(String projectName);

    ProductServiceModel saveProduct(ProductServiceModel productServiceModel, String owner);

    List<ProductViewModel> findAllByProductCategory(ProductCategoryEntity productCategory);

    void updateProduct(ProductServiceModel productServiceModel);

    void deleteProduct(Long id);

    void initializeProducts();
}
