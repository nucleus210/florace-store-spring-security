package com.nucleus.floracestore.model.view;


import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import lombok.Data;

import java.util.Set;

@Data
public class ProductCategoryViewModel {
    private Long productCategoryId;
    private String productCategoryName;
    private String productCategoryDescription;
    // TODO Unable map object --> modelMapper error
    private Set<ProductSubCategoryEntity> productSubCategories;
}
