package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("productSubCategoryRepository")
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategoryEntity, Long> {
    Optional<ProductSubCategoryEntity> findByProductSubCategoryId(long productSubCategoryId);

    Optional<ProductSubCategoryEntity> findByProductSubCategoryName(String productCategoryName);

    @Query("select s from ProductSubCategoryEntity s JOIN s.productCategory c where c.productCategoryName = :productCategoryName")
    List<ProductSubCategoryEntity> findAllSubCategoriesByCategoryName(String productCategoryName);
}
