package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;

import java.util.List;

public interface ProductSubCategoryService {
    ProductSubCategoryServiceModel getProductSubCategoryById(Long id);
    ProductSubCategoryServiceModel getProductSubCategoryBySubCategoryName(String subCategoryName);
    List<ProductSubCategoryServiceModel> getAllSubCategories();

    ProductSubCategoryServiceModel createProductSubCategory(ProductSubCategoryServiceModel subCategory,
                                                            Long categoryId,
                                                            String username);
    ProductSubCategoryServiceModel updateProductSubCategoryById(Long productSubCategoryId,
                                                                ProductSubCategoryServiceModel subCategory,
                                                                String username);
    ProductSubCategoryServiceModel deleteSubProductCategoryById(Long productSubCategoryId, String username);

    List<ProductSubCategoryServiceModel> getAllSubCategoriesByCategoryName (String categoryName);
}
