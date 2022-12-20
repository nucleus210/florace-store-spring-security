package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductStatusAssembler;
import com.nucleus.floracestore.model.dto.ProductStatusDto;
import com.nucleus.floracestore.model.service.ProductStatusServiceModel;
import com.nucleus.floracestore.model.view.ProductStatusViewModel;
import com.nucleus.floracestore.service.ProductStatusService;
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
public class ProductStatusController {
    private final ModelMapper modelMapper;
    private final ProductStatusService productStatusService;
    private final ProductStatusAssembler assembler;

    @Autowired
    public ProductStatusController(ModelMapper modelMapper,
                                   ProductStatusService productStatusService,
                                   ProductStatusAssembler assembler) {
        this.modelMapper = modelMapper;
        this.productStatusService = productStatusService;
        this.assembler = assembler;
    }

    @PostMapping("/products/statuses")
    public ResponseEntity<EntityModel<ProductStatusViewModel>> createProductStatus(@RequestBody ProductStatusDto model) {
        ProductStatusServiceModel productStatusServiceModel =
                productStatusService.createProductStatus(modelMapper.map(model, ProductStatusServiceModel.class), getCurrentLoggedUsername());
        log.info("ProductStatusController: created product status with id: " + productStatusServiceModel.getProductStatusId());
        return ResponseEntity
                .created(linkTo(methodOn(ProductStatusController.class).createProductStatus(model)).toUri())
                .body(assembler.toModel(mapToView(productStatusServiceModel)));

    }
    @PutMapping("/products/statuses/{id}")
    public ResponseEntity<EntityModel<ProductStatusViewModel>> updateProductStatus(@RequestBody ProductStatusDto model) {
        ProductStatusServiceModel productStatusServiceModel = productStatusService.updateProductStatusById(model.getProductStatusId(),
                modelMapper.map(model, ProductStatusServiceModel.class),
                getCurrentLoggedUsername());
        log.info("ProductStatusController: update product status with id: " + productStatusServiceModel.getProductStatusId());
        return ResponseEntity
                .created(linkTo(methodOn(ProductStatusController.class).updateProductStatus(model)).toUri())
                .body(assembler.toModel(mapToView(productStatusServiceModel)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/statuses/{id}")
    public ResponseEntity<EntityModel<ProductStatusViewModel>>  deleteProductStatus(@PathVariable Long productStatusId) {
        ProductStatusServiceModel productStatusServiceModel = productStatusService.deleteProductStatusById(productStatusId, getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(ProductStatusController.class).deleteProductStatus(productStatusId)).toUri())
                .body(assembler.toModel(mapToView(productStatusServiceModel)));
    }
    @GetMapping("/products/statuses/{id}")
    public ResponseEntity<EntityModel<ProductStatusViewModel>> getProductStatusById(@PathVariable Long id) {
       return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productStatusService.getProductStatusById(id))));
    }
    @GetMapping("/products/statuses/{productStatusName}/name")
    public ResponseEntity<EntityModel<ProductStatusViewModel>> getProductStatusById(@PathVariable String productStatusName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productStatusService.getProductStatusByProductStatusName(productStatusName))));
    }
    @GetMapping("/products/statuses")
    public ResponseEntity<CollectionModel<EntityModel<ProductStatusViewModel>>> getAllProductStatuses() {
        List<EntityModel<ProductStatusViewModel>> productStatuses = productStatusService.getAllProductStatuses().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productStatuses, linkTo(methodOn(ProductStatusController.class).getAllProductStatuses()).withSelfRel()));
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
    private ProductStatusViewModel mapToView(ProductStatusServiceModel model) {
        return modelMapper.map(model, ProductStatusViewModel.class);
    }
}
