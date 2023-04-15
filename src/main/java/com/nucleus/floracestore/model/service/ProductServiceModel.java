package com.nucleus.floracestore.model.service;


import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.entity.Supplier;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductServiceModel {
    private Long productId;
    private String productName;
    private BigDecimal unitQuantity;
    private BigDecimal unitSellPrice;
    private BigDecimal unitOrderPrice;
    private BigDecimal unitDiscount;
    private String productColor;
    private String productSize;
    private Float productWeight;
    private String productDescription;
    private String otherProductDetails;
    private ProductStatusServiceModel productStatus;
    private ProductCategoryServiceModel productCategory;
    private ProductSubCategoryServiceModel productSubCategory;
    private Set<StorageServiceModel> storages;
    private SupplierServiceModel supplier;
    private UserServiceModel user;
    private Set<ProductReviewEntity> productReviews;

}