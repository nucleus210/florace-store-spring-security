package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("productCategoriesRepository")
public interface ProductCategoriesRepository extends JpaRepository<ProductCategoryEntity, Long> {
    Optional<ProductCategoryEntity> findByProductCategoryName(String productCategoryName);
}
