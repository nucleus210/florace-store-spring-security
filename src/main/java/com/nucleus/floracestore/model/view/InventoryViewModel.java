package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryViewModel {
    private Long inventoryId;
    private ProductViewModel product;
    private int productQuantity;
    private Date dateInventoryPlaced;
}
