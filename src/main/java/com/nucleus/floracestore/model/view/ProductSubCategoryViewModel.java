package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class ProductSubCategoryViewModel {
    private Long productSubCategoryId;
    private String productSubCategoryName;
    private String productSubCategoryDescription;
    private ProductCategoryViewModel productCategory;
}
