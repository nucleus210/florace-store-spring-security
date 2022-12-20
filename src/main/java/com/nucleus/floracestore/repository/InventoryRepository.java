package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("inventory-repository")
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
}
