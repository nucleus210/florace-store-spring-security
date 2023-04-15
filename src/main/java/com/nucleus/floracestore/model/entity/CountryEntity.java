package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "country_id")
    private Long countryId;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "country_name")
    private String countryName;
}
