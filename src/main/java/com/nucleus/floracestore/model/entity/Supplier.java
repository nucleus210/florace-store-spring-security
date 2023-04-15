package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "company_name", unique = true)
    private String companyName;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "contact_job_title")
    private String contactJobTitle;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "company_phone_number")
    private String companyPhoneNumber;

    @Column(name = "contact_phone_number")
    private String contactPhoneNumber;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_logo_id", referencedColumnName = "resources_id")
    private StorageEntity companyLogo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user__id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address__id", referencedColumnName = "address_id")
    private AddressEntity address;

    @Column(name = "web_site")
    private String webSite;

}
