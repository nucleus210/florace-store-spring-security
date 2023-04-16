package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import lombok.Data;

import java.util.Set;

@Data
public class ProductCategoryServiceModel {
    private Long productCategoryId;
    private String productCategoryName;
    private String productCategoryDescription;
    private Set<ProductSubCategoryEntity> productSubCategories;

}
