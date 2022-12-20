package com.nucleus.floracestore.model.view;


import lombok.Data;

@Data
public class ProductCategoryViewModel {
    private Long productCategoryId;
    private String productCategoryName;
    private String productCategoryDescription;
//    private Set<ProductSubCategoryEntity> subCategories;
}
