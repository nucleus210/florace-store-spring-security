package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ContactEntity;
import com.nucleus.floracestore.model.service.ContactServiceModel;
import com.nucleus.floracestore.repository.ContactRepository;
import com.nucleus.floracestore.service.ContactService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ModelMapper modelMapper;
    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ModelMapper modelMapper, ContactRepository contactRepository) {
        this.modelMapper = modelMapper;
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactServiceModel createContact(ContactServiceModel contactServiceModel) {
        ContactEntity contactEntity = modelMapper.map(contactServiceModel, ContactEntity.class);
        return mapToService(contactRepository.save(contactEntity));
    }

    @Override
    public ContactServiceModel deleteContactById(Long contactId) {
        ContactEntity contactEntity = contactRepository.findById(contactId)
                .orElseThrow(() ->  new QueryRuntimeException("Could not find contact with id " + contactId));
        contactRepository.delete(contactEntity);
        return mapToService(contactEntity);
    }

    @Override
    public List<ContactServiceModel> getAllContacts() {
        return contactRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }
    private ContactServiceModel mapToService(ContactEntity model) {
        return modelMapper.map(model, ContactServiceModel.class);
    }

}
