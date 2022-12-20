package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class ProductReviewsViewModel {
    private Long productReviewId;
    private ProductViewModel product;
    private String username;
    private ProductRatesViewModel rate;
    private String title;
    private String content;
    private Date createdAt;
    private Date publishedAt;
}
