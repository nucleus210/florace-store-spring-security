package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class ProductSubCategoryServiceModel {
    private Long productSubCategoryId;
    private String productSubCategoryName;
    private String productSubCategoryDescription;
    private ProductCategoryServiceModel productCategory;

}
