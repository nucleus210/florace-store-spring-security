package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.service.InventoryServiceModel;
import com.nucleus.floracestore.repository.InventoryRepository;
import com.nucleus.floracestore.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryServiceModel getInventoryById(Long inventoryId) {
        return null;
    }

    @Override
    public InventoryServiceModel getInventoriesByProductId(Long productId) {
        return null;
    }

    @Override
    public List<InventoryServiceModel> getAllInvetories() {
        return null;
    }
}
