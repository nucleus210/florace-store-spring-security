package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

@Data
public class SupplierDto {
    private String companyName;
    private String contactName;
    private String contactJobTitle;
    private String emailAddress;
    private String companyPhoneNumber;
    private String contactPhoneNumber;
    private String notes;
    private StorageServiceModel companyLogo;
//    private UserServiceModel user;
    private AddressServiceModel address;
    private String webSite;
}
