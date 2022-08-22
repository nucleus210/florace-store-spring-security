package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long productId;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 20)
    private String productName;
    @Positive
    private BigDecimal productPrice;
    private String productColor;
    private String productSize;
    @Size(min = 10, max = 255)
    private String productDescription;
    @Size(min = 10, max = 255)
    private String otherProductDetails;
    @NotNull
    private ProductStatusEnum productStatus;
    @NotNull
    private String categoryName;
    private UserEntity owner;
}
