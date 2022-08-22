package com.nucleus.floracestore.model.entity;

import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;
    @Column(name = "product_price", nullable = false)
    private BigDecimal productPrice;
    @Column(name = "product_color")
    private String productColor;
    @Column(name = "product_size")
    private String productSize;
    @Column(name = "product_description", columnDefinition = "TEXT", nullable = false)
    private String productDescription;
    @Column(name = "product_details", columnDefinition = "TEXT", nullable = false)
    private String otherProductDetails;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatusEnum productStatus;
    @ManyToOne
    private ProductCategoryEntity productCategory;
    @ManyToOne
    private UserEntity owner;
    @OneToMany
    private List<StorageEntity> storageEntity;
}