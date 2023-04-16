package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

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
    @Column(name = "unit_quantity", nullable = false)
    private BigDecimal unitQuantity;
    @Column(name = "unit_sell_price", nullable = false)
    private BigDecimal unitSellPrice;
    @Column(name = "unit_order_price", nullable = false)
    private BigDecimal unitOrderPrice;
    @Column(name = "unit_discount", nullable = false)
    private BigDecimal unitDiscount;
    @Column(name = "product_color")
    private String productColor;
    @Column(name = "product_size")
    private String productSize;
    @Column(name = "product_weight")
    private Float productWeight;
    @Column(name = "product_description", columnDefinition = "TEXT", nullable = false)
    private String productDescription;
    @Column(name = "product_details", columnDefinition = "TEXT", nullable = false)
    private String otherProductDetails;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "productStatusId", referencedColumnName = "product_status_id")
    private ProductStatusEntity productStatus;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "product_category_id")
    private ProductCategoryEntity productCategory;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "product_sub_category_id")
    private ProductSubCategoryEntity productSubCategory;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user__id", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "supplier__id", referencedColumnName = "supplier_id")
    private Supplier supplier;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<StorageEntity> storages;
    @OneToMany(targetEntity = ProductReviewEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product")
    private Set<ProductReviewEntity> productReviews;
    @OneToMany(targetEntity = ProductRateEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product")
    private Set<ProductRateEntity> productRates;
    @OneToMany(targetEntity = QuestionEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product")
    private Set<QuestionEntity> productQuestions;
}