package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product_sub_categories")

public class ProductSubCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_sub_category_id")
    private Long productSubCategoryId;
    @Column(name = "product_sub_category_name", nullable = false, unique = true)
    private String productSubCategoryName;
    @Column(name = "product_sub_category_description", columnDefinition = "TEXT", nullable = false)
    private String productSubCategoryDescription;
    @ManyToOne(targetEntity = ProductCategoryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategoryEntity productCategory;
}
