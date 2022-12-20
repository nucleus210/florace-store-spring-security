package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.ProductEntity;
import lombok.Data;

@Data
public class ProductRatesDto {

    private Long productRateId;
    private int productRate;
    private ProductEntity product;
}
