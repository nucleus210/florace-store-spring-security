package com.nucleus.floracestore.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ContactDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private Date createdDate;

}
