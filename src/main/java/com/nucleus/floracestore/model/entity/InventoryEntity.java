package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "inventories")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
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
