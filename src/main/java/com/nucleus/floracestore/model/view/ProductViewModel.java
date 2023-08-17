package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.ProductRateEntity;
import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductViewModel {
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
    private ProductStatusViewModel productStatus;
    private ProductCategoryViewModel productCategory;
    // TODO Unable map object --> modelMapper error
    private ProductSubCategoryEntity productSubCategory;
    private Set<StorageViewModel> storages;
    private Set<ProductReviewEntity> productReviews;
    private Set<ProductRateEntity> productRates;
    private Set<QuestionViewModel> productQuestions;
    //    private UserEntity user;
    private boolean canDelete;

}
