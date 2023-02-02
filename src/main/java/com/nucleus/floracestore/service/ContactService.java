package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ContactServiceModel;

import java.util.List;

public interface ContactService {
    ContactServiceModel createContact(ContactServiceModel contactServiceModel);
    ContactServiceModel deleteContactById(Long contactId);
    List<ContactServiceModel> getAllContacts();
}
