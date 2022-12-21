package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("product-categories-repository")
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    Optional<ProductCategoryEntity> findByProductCategoryName(String productCategoryName);
}
