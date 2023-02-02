package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class ContactViewModel {
    private Long contactId;
    private String fistName;
    private String lastName;
    private String email;
    private String phone;
    private String comment;
    private Date createdDate;
    private Date responseDate;
}
