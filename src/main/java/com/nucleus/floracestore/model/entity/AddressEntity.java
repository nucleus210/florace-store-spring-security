package com.nucleus.floracestore.model.entity;

import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street_address", columnDefinition = "TEXT", nullable = false)
    private String streetAddress;

    @Column(name = "street_address_sec", columnDefinition = "TEXT")
    private String streetAddressSec;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province_region")
    private String stateProvinceRegion;

    @Column(name = "zip_pos")
    private String zipPostCode;

    @Column(name = "country")
    private String country;

    @Column(name = "other_address_details", columnDefinition = "TEXT")
    private String otherAddressDetails;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressTypeEnum addressType;

}
