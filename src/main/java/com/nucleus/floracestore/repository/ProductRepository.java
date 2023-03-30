package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductId(Long productId);

    Optional<ProductEntity> findByProductName(String productName);

    List<ProductEntity> findAllByProductCategory(ProductCategoryEntity productCategory);

    List<ProductEntity> findAllProductsByProductCategory_ProductCategoryName(String productCategoryName);

}