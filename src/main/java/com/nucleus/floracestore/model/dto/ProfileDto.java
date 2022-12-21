package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.AddressEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class ProfileDto {
    private Long profileId;
    @Size(min = 2, max = 20)
    private String companyName;
    @Size(min = 2, max = 20)
    private String firstName;
    @Size(min = 2, max = 20)
    private String middleName;
    @Size(min = 2, max = 20)
    private String lastName;
    private Date birthDate;
    @Size(min = 4, max = 20)
    private String gender;
    @Size(min = 4, max = 20)
    private String jobTitle;
    @Size(min = 10, max = 20)
    private String phoneNumber;
    private StorageEntity profilePhotoUrl;
    private UserEntity user;
    private AddressEntity address;
    private String webSite;
}
