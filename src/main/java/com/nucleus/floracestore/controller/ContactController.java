package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ContactAssembler;
import com.nucleus.floracestore.model.dto.ContactDto;
import com.nucleus.floracestore.model.service.ContactServiceModel;
import com.nucleus.floracestore.model.view.ContactViewModel;
import com.nucleus.floracestore.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class ContactController {
    private final ModelMapper modelMapper;
    private final ContactService contactService;
    private final ContactAssembler assembler;
    @Autowired
    public ContactController(ModelMapper modelMapper,
                             ContactService contactService,
                             ContactAssembler assembler) {
        this.modelMapper = modelMapper;
        this.contactService = contactService;
        this.assembler = assembler;
    }

    @PostMapping("/contacts")
    public ResponseEntity<EntityModel<ContactViewModel>> createContact (@Valid @RequestBody ContactDto contactDto) {
        log.info("ContactController: created new contact with id ");

        return ResponseEntity
                .created(linkTo(methodOn(ContactController.class).createContact(contactDto)).toUri())
                .body(assembler.toModel(mapView(contactService.createContact(modelMapper.map(contactDto, ContactServiceModel.class)))));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @GetMapping("/contacts")
    public ResponseEntity<CollectionModel<EntityModel<ContactViewModel>>> getAllContacts() {
        log.info("ContactController: get all contacts");
        List<EntityModel<ContactViewModel>> contacts = contactService.getAllContacts().stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(contacts, linkTo(methodOn(ContactController.class).getAllContacts()).withSelfRel()));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<EntityModel<ContactViewModel>> deleteContactById(@PathVariable Long contactId) {
        log.info("ContactController: delete contact with id " + contactId);
        EntityModel<ContactViewModel> ContactViewModel = assembler.toModel(mapView(contactService.deleteContactById(contactId)));
        return ResponseEntity.status(HttpStatus.OK).body(ContactViewModel);
    }

    private ContactViewModel mapView(ContactServiceModel model) {
        return modelMapper.map(model, ContactViewModel.class);
    }

}
