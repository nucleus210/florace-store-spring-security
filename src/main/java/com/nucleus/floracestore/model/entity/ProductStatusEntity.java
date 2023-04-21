package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "product_status")
public class ProductStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "product_status_id")
    private Long productStatusId;
    @Column(name = "product_status_name")
    private String productStatusName;
    @Column(name = "product_status_description")
    private String productStatusDescription;
}
