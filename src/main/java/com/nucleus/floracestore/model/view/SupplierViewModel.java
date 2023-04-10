package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import lombok.Data;

@Data
public class SupplierViewModel {
    private Long supplierId;
    private String companyName;
    private String contactName;
    private String contactJobTitle;
    private String emailAddress;
    private String companyPhoneNumber;
    private String contactPhoneNumber;
    private String notes;
    private StorageViewModel companyLogo;
//    private UserServiceModel user;
    private AddressViewModel address;
    private String webSite;
}
