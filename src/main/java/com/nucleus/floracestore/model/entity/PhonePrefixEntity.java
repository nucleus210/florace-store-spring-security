package com.nucleus.floracestore.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phones_prefixes")
public class PhonePrefixEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "phone_prefix_id")
    private Long phonePrefixId;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "prefix")
    private String prefix;

}
