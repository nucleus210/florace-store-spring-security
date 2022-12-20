package com.nucleus.floracestore.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "contact_job_title")
    private String contactJobTitle;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "company_phone_number", nullable = false)
    private String companyPhoneNumber;

    @Column(name = "contact_phone_number")
    private String contactPhoneNumber;

    @Column(name = "notes")
    private String notes;

    @OneToOne(optional = false)
    @JoinColumn(name = "profile_photo_id",
            referencedColumnName = "resources_id")
    private StorageEntity companyLogo;

    @OneToOne(optional = false)
    @JoinColumn(referencedColumnName = "user_id", name = "user__id")
    private UserEntity user;

    @OneToOne(optional = false)
    @JoinColumn(name = "profile_address_id",
            referencedColumnName = "address_id")
    private AddressEntity address;

    @Column(name = "web_site")
    private String webSite;

}
