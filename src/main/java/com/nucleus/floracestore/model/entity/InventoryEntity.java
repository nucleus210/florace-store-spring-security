package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "inventories")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventory_id")
    private Long inventoryId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "_product_id", referencedColumnName = "product_id")
    private ProductEntity product;
    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_inventory_placed")
    private Date dateInventoryPlaced;

}
