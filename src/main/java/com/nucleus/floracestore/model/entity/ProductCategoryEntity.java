package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product_categories")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "product_category_id")
    private Long productCategoryId;
    @Column(name = "product_category_name", nullable = false, unique = true)
    private String productCategoryName;
    @Column(name = "product_category_description", columnDefinition = "TEXT", nullable = false)
    private String productCategoryDescription;
    @JsonIgnore
    @OneToMany(targetEntity = ProductSubCategoryEntity.class,
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY,
            mappedBy = "productCategory")
    private Set<ProductSubCategoryEntity> productSubCategories;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "_resource_id", referencedColumnName = "resource_id")
    private StorageEntity storage;
}
