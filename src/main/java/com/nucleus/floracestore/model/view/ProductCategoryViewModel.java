package com.nucleus.floracestore.model.view;


import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

import java.util.Set;

@Data
public class ProductCategoryViewModel {
    private Long productCategoryId;
    private String productCategoryName;
    private String productCategoryDescription;
    // TODO Unable map object --> modelMapper error
    private Set<ProductSubCategoryEntity> productSubCategories;
    private StorageViewModel storage;
}
