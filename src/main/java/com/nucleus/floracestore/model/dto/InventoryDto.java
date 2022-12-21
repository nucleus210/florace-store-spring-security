package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.ProductServiceModel;
import lombok.Data;

import java.util.Date;

@Data
public class InventoryDto {
    private ProductServiceModel product;
    private int productQuantity;
    private Date dateInventoryPlaced;

}
