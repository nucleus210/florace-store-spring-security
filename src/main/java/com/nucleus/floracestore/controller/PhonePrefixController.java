package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.PhonePrefixAssembler;
import com.nucleus.floracestore.model.dto.CountryDTO;
import com.nucleus.floracestore.model.dto.PhonePrefixDTO;
import com.nucleus.floracestore.model.service.CountryServiceModel;
import com.nucleus.floracestore.model.service.PhonePrefixServiceModel;
import com.nucleus.floracestore.model.view.CountryViewModel;
import com.nucleus.floracestore.model.view.PhonePrefixViewModel;
import com.nucleus.floracestore.service.PhonePrefixService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public PhonePrefixController(ModelMapper modelMapper, PhonePrefixService phonePrefixService, PhonePrefixAssembler assembler) {
        this.modelMapper = modelMapper;
        this.phonePrefixService = phonePrefixService;
        this.assembler = assembler;
    }


    @PostMapping("/phones-prefixes")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> createPhonePrefix(@RequestBody PhonePrefixDTO model) {
        PhonePrefixServiceModel phonePrefixServiceModel = phonePrefixService.createPhonePrefix(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(PhonePrefixController.class).createPhonePrefix(model)).toUri())
                .body(assembler.toModel(mapToView(phonePrefixServiceModel)));
    }

    @PutMapping("/phones-prefixes/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> updatePhonePrefix(@RequestBody PhonePrefixDTO model, @PathVariable Long phonePrefixId) {
        PhonePrefixServiceModel phonePrefixServiceModel = phonePrefixService.updatePhonePrefixById(mapToService(model), phonePrefixId);
        return ResponseEntity
                .created(linkTo(methodOn(PhonePrefixController.class).updatePhonePrefix(model, phonePrefixId)).toUri())
                .body(assembler.toModel(mapToView(phonePrefixServiceModel)));
    }
    @DeleteMapping("/phones-prefixes/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> deletePhonePrefix(@PathVariable Long phonePrefixId) {
        EntityModel<PhonePrefixViewModel> phonePrefixViewModel = assembler.toModel(mapToView(phonePrefixService.deletePhonePrefixById(phonePrefixId)));
        return ResponseEntity.status(HttpStatus.OK).body(phonePrefixViewModel);
    }

    @GetMapping("/phones-prefixes/all")
    public ResponseEntity<CollectionModel<EntityModel<PhonePrefixViewModel>>> getAllPhonePrefixes() {
        List<EntityModel<PhonePrefixViewModel>> phonePrefixes = phonePrefixService.getAllPhonePrefixes().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(phonePrefixes, linkTo(methodOn(PhonePrefixController.class).getAllPhonePrefixes()).withSelfRel()));
    }
    @GetMapping("/phones-prefixes/search/id/{phonePrefixId}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixById(@PathVariable Long phonePrefixId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixById(phonePrefixId))));
    }
    @GetMapping("/phones-prefixes/search/country-name/{countryName}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixByCountryName(@PathVariable String countryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixByCountryName(countryName))));
    }
    @GetMapping("/phones-prefixes/search/prefix/{prefix}")
    public ResponseEntity<EntityModel<PhonePrefixViewModel>> getPhonePrefixByPhonePrefix(@PathVariable String prefix) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(phonePrefixService.getPhonePrefixByPrefix(prefix))));
    }
    private PhonePrefixViewModel mapToView(PhonePrefixServiceModel model) {
        return modelMapper.map(model, PhonePrefixViewModel.class);
    }

    private PhonePrefixServiceModel mapToService(PhonePrefixDTO model) {
        return modelMapper.map(model, PhonePrefixServiceModel.class);
    }

}