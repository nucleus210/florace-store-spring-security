package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Float profileId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "work_phone_number")
    private String workPhoneNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_photo_id",
            referencedColumnName = "resources_id")
    private StorageEntity profilePhotoUrl;

    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "user_id", name = "user__id")
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_address_id",
            referencedColumnName = "address_id")
    private AddressEntity address;

    @Column(name = "web_site")
    private String webSite;
}

