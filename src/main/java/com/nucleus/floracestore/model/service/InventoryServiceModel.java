package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryServiceModel {
    private Long inventoryId;
    private ProductServiceModel product;
    private int productQuantity;
    private Date dateInventoryPlaced;

}
