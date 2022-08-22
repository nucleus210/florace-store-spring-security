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

    @Column(name = "first_name", nullable = false)
    private String firstName;

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

    @OneToOne
    private StorageEntity profilePhotoUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    private AddressEntity address;

    @OneToOne
    private StorageEntity profilePhoto;
}

