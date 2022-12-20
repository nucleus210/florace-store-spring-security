package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;

@Data
public class ProductReviewsServiceModel {
    private Long productReviewId;
    private ProductServiceModel product;
    private UserServiceModel user;
    private ProductRatesServiceModel rate;
    private String title;
    private String content;
    private Date createdAt;
    private Date publishedAt;
}
