package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductViewModel {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productColor;
    private String productSize;
    private String productDescription;
    private String otherProductDetails;
    private ProductStatusEnum productStatus;
    private ProductCategoryEntity productCategory;
    private UserEntity owner;
    private StorageEntity storageEntity;
    private boolean canDelete;
}
