package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.model.view.ProductStatusViewModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
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
    private ProductStatusViewModel productStatus;
    @NotNull
    private String categoryName;
    @NotNull
    private ProductCategoryServiceModel productCategory;
    @NotNull
    private ProductSubCategoryServiceModel productSubCategory;
    private Set<StorageServiceModel> storages;
    private Set<ProductReviewEntity> productReviews;

}
