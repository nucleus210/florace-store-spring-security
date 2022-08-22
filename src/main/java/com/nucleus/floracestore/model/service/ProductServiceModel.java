package com.nucleus.floracestore.model.service;


import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductServiceModel {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productColor;
    private String productSize;
    private String productDescription;
    private String otherProductDetails;
    private ProductStatusEnum productStatus;
    private ProductCategoryEntity productCategory;
    private List<StorageEntity> storageEntity;
    private UserEntity owner;
}