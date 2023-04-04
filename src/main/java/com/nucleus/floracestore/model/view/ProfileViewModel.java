package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileViewModel {
    private Long profileId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String jobTitle;
    private String phoneNumber;
    private StorageViewModel profilePhotoUrl;
    private String username;
    private AddressViewModel address;
}
