package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.InventoryServiceModel;

import java.util.List;

public interface InventoryService {
    InventoryServiceModel getInventoryById(Long inventoryId);

    InventoryServiceModel getInventoriesByProductId(Long productId);

    List<InventoryServiceModel> getAllInvetories();
}
