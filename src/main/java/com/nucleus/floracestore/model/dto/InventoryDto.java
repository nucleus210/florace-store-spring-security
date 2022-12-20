package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.ProductEntity;
import lombok.Data;

import java.util.Date;

@Data
public class InventoryDto {
    private ProductEntity product;
    private int productQuantity;
    private Date dateInventoryPlaced;

}
