package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "address_types")
public class AddressTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "address_type_id")
    private Long addressTypeId;
    @Column(name = "address_type_name")
    private String addressTypeName;
    @Column(name = "address_type_description")
    private String addressTypeDescription;
}
