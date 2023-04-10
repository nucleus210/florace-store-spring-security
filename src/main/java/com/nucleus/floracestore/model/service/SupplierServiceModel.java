package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class SupplierServiceModel {
    private Long supplierId;
    private String companyName;
    private String contactName;
    private String contactJobTitle;
    private String emailAddress;
    private String companyPhoneNumber;
    private String contactPhoneNumber;
    private String notes;
    private StorageServiceModel companyLogo;
    private UserServiceModel user;
    private AddressServiceModel address;
    private String webSite;
}
