package com.nucleus.floracestore.model.service;


import com.nucleus.floracestore.model.entity.ProductRateEntity;
import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
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
    // TODO Unable map object --> modelMapper error
    private ProductSubCategoryEntity productSubCategory;
    private UserServiceModel user;
    private SupplierServiceModel supplier;
    private Set<StorageServiceModel> storages;
    private Set<ProductReviewEntity> productReviews;
    private Set<ProductRateEntity> productRates;
    private Set<QuestionServiceModel> productQuestions;

}