package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class ProductRatesServiceModel {
    private Long productRateId;
    private int productRate;
    private ProductServiceModel product;
    private UserServiceModel user;
}
