package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.service.*;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductDto {
    private Long productId;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String productName;
    @Positive
    private BigDecimal unitQuantity;
    @Positive
    private BigDecimal unitSellPrice;
    @Positive
    private BigDecimal unitOrderPrice;
    @Positive
    private BigDecimal unitDiscount;
    private String productColor;
    private String productSize;
    private Float productWeight;
    @Size(min = 10, max = 255)
    private String productDescription;
    @Size(min = 10, max = 500)
    private String otherProductDetails;
    @NotNull
    private String productStatus;
    @NotNull
    private String productCategory;
    @NotNull
    private String productSubCategory;
    private SupplierServiceModel supplier;
    private Set<StorageServiceModel> storages;
    private Set<ProductReviewEntity> productReviews;

}
