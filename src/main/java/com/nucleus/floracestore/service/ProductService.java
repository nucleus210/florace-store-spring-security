package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductServiceModel getProductById(Long projectId);
    ProductServiceModel getByProductName(String projectName);
    List<ProductServiceModel> getAllProducts();
    ProductServiceModel saveProduct(ProductServiceModel productServiceModel, String owner);
    void updateProduct(ProductServiceModel productServiceModel);
    void deleteProduct(Long id);
    ProductViewModel getByIdAndCurrentUser(Long id, String owner);
    ProductViewModel getById(Long id);
    List<ProductServiceModel> getAllByProductCategory(String productCategory);

}
