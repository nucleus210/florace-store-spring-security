package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.PhonePrefixAssembler;
import com.nucleus.floracestore.model.dto.PhonePrefixDto;
import com.nucleus.floracestore.model.service.PhonePrefixServiceModel;
import com.nucleus.floracestore.model.view.PhonePrefixViewModel;
import com.nucleus.floracestore.service.PhonePrefixService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class PhonePrefixController {
    private final ModelMapper modelMapper;
    private final PhonePrefixService phonePrefixService;
    private final PhonePrefixAssembler assembler;

    @Autowired
    public PhonePrefixController(ModelMapper modelMapper,
                                 PhonePrefixService phonePrefixService,
                                 PhonePrefixAssembler assembler) {
        this.modelMapper = modelMapper;
        this.phonePrefixService = phonePrefixService;
        this.assembler = assembler;
    }


    @PostMapping("/phone-prefixes")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> createPhonePrefix(@RequestBody PhonePrefixDto model) {
        PhonePrefixServiceModel phonePrefixServiceModel = phonePrefixService.createPhonePrefix(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(PhonePrefixController.class).createPhonePrefix(model)).toUri())
                .body(assembler.toModel(mapToView(phonePrefixServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PutMapping("/phone-prefixes/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> updatePhonePrefix(@RequestBody PhonePrefixDto model, @PathVariable Long phonePrefixId) {
        PhonePrefixServiceModel phonePrefixServiceModel = phonePrefixService.updatePhonePrefixById(mapToService(model), phonePrefixId);
        return ResponseEntity
                .created(linkTo(methodOn(PhonePrefixController.class).updatePhonePrefix(model, phonePrefixId)).toUri())
                .body(assembler.toModel(mapToView(phonePrefixServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/phone-prefixes/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> deletePhonePrefix(@PathVariable Long phonePrefixId) {
        EntityModel<PhonePrefixViewModel> phonePrefixViewModel = assembler.toModel(mapToView(phonePrefixService.deletePhonePrefixById(phonePrefixId)));
        return ResponseEntity.status(HttpStatus.OK).body(phonePrefixViewModel);
    }

    @GetMapping("/phone-prefixes")
    public ResponseEntity<CollectionModel<EntityModel<PhonePrefixViewModel>>> getAllPhonePrefixes() {
        List<EntityModel<PhonePrefixViewModel>> phonePrefixes = phonePrefixService.getAllPhonePrefixes().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(phonePrefixes, linkTo(methodOn(PhonePrefixController.class).getAllPhonePrefixes()).withSelfRel()));
    }

    @GetMapping("/phone-prefixes/search/id/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixById(@PathVariable Long phonePrefixId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixById(phonePrefixId))));
    }

    @GetMapping("/phone-prefixes/search/country-name/{countryName}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixByCountryName(@PathVariable String countryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixByCountryName(countryName))));
    }

    @GetMapping("/phone-prefixes/search/prefix/{prefix}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixByPhonePrefix(@PathVariable String prefix) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixByPrefix(prefix))));
    }

    private PhonePrefixViewModel mapToView(PhonePrefixServiceModel model) {
        return modelMapper.map(model, PhonePrefixViewModel.class);
    }

    private PhonePrefixServiceModel mapToService(PhonePrefixDto model) {
        return modelMapper.map(model, PhonePrefixServiceModel.class);
    }

}