package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.AddressTypeAssembler;
import com.nucleus.floracestore.model.dto.AddressTypeDto;
import com.nucleus.floracestore.model.service.AddressTypeServiceModel;
import com.nucleus.floracestore.model.view.AddressTypeViewModel;
import com.nucleus.floracestore.service.AddressTypeService;
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
public class AddressTypeController {

    private final ModelMapper modelMapper;
    private final AddressTypeService addressTypeService;
    private final AddressTypeAssembler assembler;

    @Autowired
    public AddressTypeController(ModelMapper modelMapper,
                                 AddressTypeService addressTypeService,
                                 AddressTypeAssembler assembler) {
        this.modelMapper = modelMapper;
        this.addressTypeService = addressTypeService;
        this.assembler = assembler;
    }

    @PostMapping("/addresses/address-types")
    public ResponseEntity<EntityModel<AddressTypeViewModel>> createAddressType(@RequestBody AddressTypeDto model) {
        AddressTypeServiceModel addressTypeServiceModel = addressTypeService.createAddressType(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(AddressTypeController.class).createAddressType(model)).toUri())
                .body(assembler.toModel(mapToView(addressTypeServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PutMapping("/addresses/address-types/{addressTypeId}")
    public ResponseEntity<EntityModel<AddressTypeViewModel>> updateAddressType(@RequestBody AddressTypeDto model, @PathVariable Long addressTypeId) {
        AddressTypeServiceModel addressTypeServiceModel = addressTypeService.updateAddressTypeById(mapToService(model), addressTypeId);
        return ResponseEntity
                .created(linkTo(methodOn(AddressTypeController.class).updateAddressType(model, addressTypeId)).toUri())
                .body(assembler.toModel(mapToView(addressTypeServiceModel)));
    }
    @DeleteMapping("/addresses/address-types/{addressTypeId}")
    public ResponseEntity<EntityModel<AddressTypeViewModel>> deleteAddressType(@PathVariable Long addressTypeId) {
        EntityModel<AddressTypeViewModel> addressTypeViewModel = assembler.toModel(mapToView(addressTypeService.deleteAddressTypeById(addressTypeId)));
        return ResponseEntity.status(HttpStatus.OK).body(addressTypeViewModel);
    }

    @GetMapping("/addresses/address-types")
    public ResponseEntity<CollectionModel<EntityModel<AddressTypeViewModel>>> getAllAddressTypes() {
        List<EntityModel<AddressTypeViewModel>> addressTypes = addressTypeService.getAllAddressTypes().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(addressTypes, linkTo(methodOn(AddressTypeController.class).getAllAddressTypes()).withSelfRel()));
    }
    @GetMapping("/addresses/address-types/search/id/{addressTypeId}")
    public ResponseEntity<EntityModel<AddressTypeViewModel>> getAddressTypeById(@PathVariable Long addressTypeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(addressTypeService.getAddressTypeById(addressTypeId))));
    }
    @GetMapping("/addresses/address-types/search/address-type-name/{addressTypeName}")
    public ResponseEntity<EntityModel<AddressTypeViewModel>> getAddressTypeByName(@PathVariable String addressTypeName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(addressTypeService.getAddressTypeByName(addressTypeName))));
    }

    private AddressTypeViewModel mapToView(AddressTypeServiceModel model) {
        return modelMapper.map(model, AddressTypeViewModel.class);
    }

    private AddressTypeServiceModel mapToService(AddressTypeDto model) {
        return modelMapper.map(model, AddressTypeServiceModel.class);
    }
}
