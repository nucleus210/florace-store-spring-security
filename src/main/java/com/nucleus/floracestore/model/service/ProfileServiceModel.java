package com.nucleus.floracestore.model.service;

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
    private StorageServiceModel profilePhotoUrl;
    private UserServiceModel user;
    private AddressServiceModel address;
}
