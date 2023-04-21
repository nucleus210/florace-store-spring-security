package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.AddressAssembler;
import com.nucleus.floracestore.model.dto.AddressDto;
import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.model.service.AddressTypeServiceModel;
import com.nucleus.floracestore.model.service.CountryServiceModel;
import com.nucleus.floracestore.model.view.AddressViewModel;
import com.nucleus.floracestore.service.AddressService;
import com.nucleus.floracestore.service.AddressTypeService;
import com.nucleus.floracestore.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
@RestController
public class AddressController {
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final AddressTypeService addressTypeService;
    private final AddressAssembler assembler;
    private final CountryService countryService;
    @Autowired
    public AddressController(ModelMapper modelMapper,
                             AddressService addressService,
                             AddressTypeService addressTypeService, AddressAssembler assembler, CountryService countryService) {
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.addressTypeService = addressTypeService;
        this.assembler = assembler;
        this.countryService = countryService;
    }

    
    @ModelAttribute("addressModel")
    public AddressDto addressModel() {
        return new AddressDto();
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/addresses/add")
    public ResponseEntity<EntityModel<AddressViewModel>> createAddress(@Valid AddressDto addressModel) {
        modelMapper.typeMap(AddressDto.class, AddressServiceModel.class).addMappings(mapper -> mapper.skip(AddressServiceModel::setAddressType));
        AddressServiceModel serviceModel = modelMapper.map(addressModel, AddressServiceModel.class);
        AddressTypeServiceModel addressType = addressTypeService.getAddressTypeByName(AddressTypeEnum.valueOf(addressModel.getAddressType()).name());
        CountryServiceModel country = countryService.getCountryByCountryName(addressModel.getCountry());
        serviceModel.setAddressType(addressType);
        serviceModel.setCountry(country);

        return ResponseEntity
                .created(linkTo(methodOn(AddressController.class).createAddress(addressModel)).toUri())
                .body(assembler.toModel(mapToView(addressService.createAddress(serviceModel))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<EntityModel<AddressViewModel>> updateAddress(@RequestBody AddressDto model, @PathVariable Long addressId) {
        AddressServiceModel addressServiceModel = addressService.updateAddress(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(AddressController.class).updateAddress(model, addressId)).toUri())
                .body(assembler.toModel(mapToView(addressServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<EntityModel<?>> deleteAddress(@PathVariable Long addressId) {
        EntityModel<AddressViewModel> address = assembler.toModel(mapToView(addressService.deleteAddress(addressId)));
        return ResponseEntity.status(HttpStatus.OK).body(address);
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/addresses/{addressId}")
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
