package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ProductReviewsServiceModel;

import java.util.List;
import java.util.Optional;

public interface ProductReviewsService {
    ProductReviewsServiceModel writeProductReview(ProductReviewsServiceModel model);

    ProductReviewsServiceModel createProductReview(ProductReviewsServiceModel model, Long productId, String username);
    List<ProductReviewsServiceModel> getAllProductReviewsByProductId(Long productId);
    ProductReviewsServiceModel getProductReviewById(Long productReviewId);
    Optional<ProductReviewsServiceModel> getProductReviewByProductIdAndUsername(Long productId, String username);

}
