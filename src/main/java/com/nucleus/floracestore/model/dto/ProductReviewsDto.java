package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.ProductEntity;
import com.nucleus.floracestore.model.entity.ProductRateEntity;
import lombok.Data;

import java.util.Date;

@Data
public class ProductReviewsDto {

    private Long productReviewId;
    private ProductEntity product;
    private ProductRateEntity rate;
    private String title;
    private String content;
    private Date createdAt;
    private Date publishedAt;
}
