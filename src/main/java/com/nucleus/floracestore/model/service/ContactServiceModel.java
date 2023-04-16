package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;

@Data
public class ContactServiceModel {
    private Long contactId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private Date createdDate;
    private Date responseDate;

}
