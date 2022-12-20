package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product_categories")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_category_id")
    private Long productCategoryId;
    @Column(name = "product_category_name", nullable = false, unique = true)
    private String productCategoryName;
    @Column(name = "product_category_description", columnDefinition = "TEXT", nullable = false)
    private String productCategoryDescription;
    @OneToMany(targetEntity = ProductSubCategoryEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "productCategory")
    private Set<ProductSubCategoryEntity> productSubCategories;

}
