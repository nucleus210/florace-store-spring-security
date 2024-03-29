package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.SupplierAssembler;
import com.nucleus.floracestore.model.dto.SupplierDto;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.model.service.SupplierServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.model.view.SupplierViewModel;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.service.SupplierService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class SupplierController {

    private final ModelMapper modelMapper;
    private final SupplierService supplierService;
    private final SupplierAssembler assembler;
    private final StorageService storageService;
    private final UserService userService;
    @Autowired
    public SupplierController(ModelMapper modelMapper,
                              SupplierService supplierService,
                              SupplierAssembler assembler,
                              StorageService storageService,
                              UserService userService) {
        this.modelMapper = modelMapper;
        this.supplierService = supplierService;
        this.assembler = assembler;
        this.storageService = storageService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PostMapping("/suppliers")
    public ResponseEntity<EntityModel<SupplierViewModel>> createSupplier(@RequestBody SupplierDto model) {

        if(model.getCompanyLogo() == null) {
            StorageServiceModel storage = storageService.getByName("blank_profile_picture.jpg");
            model.setCompanyLogo(storage);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserServiceModel userEntity = userService.findByUsername(auth.getPrincipal().toString());

        log.info("Login user: {}" + auth.getPrincipal().toString());
        log.info("Login user: " + userEntity);

        SupplierServiceModel supplierServiceModel = mapToService(model);
        supplierServiceModel.setUser(userEntity);

        return ResponseEntity
                .created(linkTo(methodOn(SupplierController.class).createSupplier(model)).toUri())
                .body(assembler.toModel(mapToView(supplierService.createSupplier(supplierServiceModel))));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/suppliers/{supplierId}")
    public ResponseEntity<EntityModel<SupplierViewModel>> getSupplierById(@PathVariable Long supplierId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(supplierService.getSupplierById(supplierId))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PutMapping("/suppliers/{supplierId}")
    public ResponseEntity<EntityModel<SupplierViewModel>> updateSupplierById(@RequestBody SupplierDto model, @PathVariable Long supplierId) {

        SupplierServiceModel supplierServiceModel = supplierService.updateSupplier(mapToService(model), supplierId);
        return ResponseEntity
                .created(linkTo(methodOn(SupplierController.class).updateSupplierById(model, supplierId)).toUri())
                .body(assembler.toModel(mapToView(supplierServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/suppliers/{supplierId}")
    public ResponseEntity<EntityModel<SupplierViewModel>> deleteSupplierById(@PathVariable Long supplierId) {
        EntityModel<SupplierViewModel> SupplierViewModel = assembler.toModel(mapToView(supplierService.deleteSupplierById(supplierId)));
        return ResponseEntity.status(HttpStatus.OK).body(SupplierViewModel);
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/suppliers")
    public ResponseEntity<CollectionModel<EntityModel<SupplierViewModel>>> getAllSuppliers() {
        List<EntityModel<SupplierViewModel>> suppliers = supplierService.getAllSuppliers().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(suppliers, linkTo(methodOn(SupplierController.class).getAllSuppliers()).withSelfRel()));
    }

    private SupplierViewModel mapToView(SupplierServiceModel model) {
        return modelMapper.map(model, SupplierViewModel.class);
    }

    private SupplierServiceModel mapToService(SupplierDto model) {
        return modelMapper.map(model, SupplierServiceModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
