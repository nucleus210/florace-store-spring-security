package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class ProductRatesViewModel {
    private Long productRateId;
    private int productRate;
    private ProductViewModel product;
    private String username;
}
