package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProductCategoryDto {

    private String productCategoryName;
    private String productCategoryDescription;
    private Set<ProductSubCategoryServiceModel> subCategories = new HashSet<>();
    private StorageServiceModel storage;
}
