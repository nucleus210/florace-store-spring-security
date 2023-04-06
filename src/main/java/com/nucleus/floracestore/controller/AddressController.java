package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.AddressAssembler;
import com.nucleus.floracestore.model.dto.AddressDto;
import com.nucleus.floracestore.model.dto.AddressDto;
import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.view.AddressViewModel;
import com.nucleus.floracestore.model.view.AnswerViewModel;
import com.nucleus.floracestore.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
@RestController
public class AddressController {
    private final ModelMapper modelMapper;

    private final AddressService addressService;
    private final AddressAssembler assembler;
    @Autowired
    public AddressController(ModelMapper modelMapper, 
                             AddressService addressService, 
                             AddressAssembler assembler) {
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.assembler = assembler;
    }

    
    @ModelAttribute("addressModel")
    public AddressDto addressModel() {
        return new AddressDto();
    }
    
    @PostMapping("/address/add")
    public ResponseEntity<EntityModel<AddressViewModel>> createAddress(@Valid AddressDto addressModel) {
        
        modelMapper.typeMap(AddressDto.class, AddressServiceModel.class).addMappings(mapper -> mapper.skip(AddressServiceModel::setAddressType));
        AddressServiceModel serviceModel = modelMapper.map(addressModel, AddressServiceModel.class);
        serviceModel.setAddressType(AddressTypeEnum.valueOf(addressModel.getAddressType()));

        return ResponseEntity
                .created(linkTo(methodOn(AddressController.class).createAddress(addressModel)).toUri())
                .body(assembler.toModel(mapToView(addressService.createAddress(serviceModel))));
    }
    @PutMapping("/address/{addressId}")
    public ResponseEntity<EntityModel<AddressViewModel>> updateAddress(@RequestBody AddressDto model, @PathVariable Long addressId) {
        AddressServiceModel addressServiceModel = addressService.updateAddress(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(AddressController.class).updateAddress(model, addressId)).toUri())
                .body(assembler.toModel(mapToView(addressServiceModel)));
    }
    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<EntityModel<?>> deleteAddress(@PathVariable Long addressId) {
        EntityModel<AddressViewModel> address = assembler.toModel(mapToView(addressService.deleteAddress(addressId)));
        return ResponseEntity.status(HttpStatus.OK).body(address);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<EntityModel<AddressViewModel>> getAddressById(@PathVariable Long AddressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(addressService.getAddressById(AddressId))));
    }

    private AddressViewModel mapToView(AddressServiceModel model) {
        return modelMapper.map(model, AddressViewModel.class);
    }

    private AddressServiceModel mapToService(AddressDto model) {
        return modelMapper.map(model, AddressServiceModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }


}
