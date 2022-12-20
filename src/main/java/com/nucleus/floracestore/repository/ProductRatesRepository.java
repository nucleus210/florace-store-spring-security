package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("productRatesRepository")
public interface ProductRatesRepository extends JpaRepository<ProductRateEntity, Long> {

    @Query("select r from ProductRateEntity r JOIN FETCH r.product p where p.productId = :productId")
    List<ProductRateEntity> findAllProductRatesByProductId(Long productId);

    @Query("select r from ProductRateEntity r JOIN r.product p JOIN r.user u where p.productId = :productId and u.username = :username")
    Optional<ProductRateEntity> findProductRateByProductIdAndUsername(Long productId, String username);
}
