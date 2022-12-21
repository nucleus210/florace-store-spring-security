package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("productReviewsRepository")
public interface ProductReviewsRepository extends JpaRepository<ProductReviewEntity, Long> {
    @Query("select r from ProductReviewEntity r JOIN FETCH r.product p where p.productId = :productId")
    List<ProductReviewEntity> findAllProductReviewsByProductId(Long productId);

    @Query("select r from ProductReviewEntity r JOIN r.product p JOIN r.user u where p.productId = :productId and u.username = :username")
    Optional<ProductReviewEntity> findByProductIdAndUsername(Long productId, String username);
}
