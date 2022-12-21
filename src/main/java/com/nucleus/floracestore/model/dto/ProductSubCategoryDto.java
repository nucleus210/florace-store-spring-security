package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import lombok.Data;

@Data
public class ProductSubCategoryDto {
    private String productSubCategoryName;
    private String productSubCategoryDescription;
    private ProductCategoryServiceModel productCategory;
}
