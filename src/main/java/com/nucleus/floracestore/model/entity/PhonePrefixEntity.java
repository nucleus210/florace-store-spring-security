package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "phones_prefixes")
public class PhonePrefixEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "phone_prefix_id")
    private Long phonePrefixId;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "prefix")
    private String prefix;

}
