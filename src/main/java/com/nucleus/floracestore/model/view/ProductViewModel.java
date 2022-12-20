package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.ProductReviewEntity;
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
    private ProductSubCategoryViewModel productSubCategory;
    private Set<StorageViewModel> storages;
    private Set<ProductReviewEntity> productReviews;
    //    private UserEntity user;
    private boolean canDelete;

}
