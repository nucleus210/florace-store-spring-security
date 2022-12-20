package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProductStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("productStatusRepository")
public interface ProductStatusRepository extends JpaRepository<ProductStatusEntity, Long> {

    Optional<ProductStatusEntity> findByProductStatusName(String productStatusName);
}
