package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfileServiceModel {
    private Long profileId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String jobTitle;
    private String phoneNumber;
    private StorageEntity profilePhotoUrl;
    private UserEntity user;
    private AddressEntity address;
}
