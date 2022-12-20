package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductRatesAssembler;
import com.nucleus.floracestore.model.dto.ProductRatesDto;
import com.nucleus.floracestore.model.service.ProductRatesServiceModel;
import com.nucleus.floracestore.model.view.ProductRatesViewModel;
import com.nucleus.floracestore.service.ProductRatesService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class ProductRatesController {

    private final ModelMapper modelMapper;
    private final ProductRatesService productRatesService;
    private final ProductRatesAssembler assembler;

    @Autowired
    public ProductRatesController(ModelMapper modelMapper,
                                  ProductRatesService productRatesService,
                                  ProductRatesAssembler assembler) {
        this.modelMapper = modelMapper;
        this.productRatesService = productRatesService;
        this.assembler = assembler;
    }

    @PostMapping("/products/{productId}/rates")
    public ResponseEntity<EntityModel<ProductRatesViewModel>> rateProduct(@RequestBody ProductRatesDto model,
                                                                           @PathVariable Long productId) {
        ProductRatesServiceModel productRatesServiceModel =
                productRatesService.rateProduct(modelMapper.map(model, ProductRatesServiceModel.class),
                                                productId,
                                                getCurrentLoggedUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(productRatesServiceModel)));
    }
    @GetMapping("/products/{productId}/users/{username}/rates")
    public ResponseEntity<EntityModel<ProductRatesViewModel>> getProductRateByProductIdAndUsername(@PathVariable Long productId, @PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(productRatesService.getProductRateByProductIdAndUsername(productId, username))));

    }

    @GetMapping("/products/rates/{productRateId}")
    public ResponseEntity<EntityModel<ProductRatesViewModel>> getProductRatesById(@PathVariable Long productRateId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(productRatesService.getProductRateById(productRateId))));
    }

    @GetMapping(value = "/products/{productId}/rates")
    public ResponseEntity<CollectionModel<EntityModel<ProductRatesViewModel>>> getAllProductRatesByProductId(@PathVariable Long productId) {
        List<EntityModel<ProductRatesViewModel>> productRates = productRatesService.getAllProductRatesByProductId(productId).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productRates, linkTo(methodOn(ProductRatesController.class).getAllProductRatesByProductId(productId)).withSelfRel()));
    }

    private ProductRatesViewModel mapView(ProductRatesServiceModel productRatesServiceModel) {
        modelMapper.typeMap(ProductRatesServiceModel.class, ProductRatesViewModel.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getUsername(), ProductRatesViewModel::setUsername));
        return this.modelMapper.map(productRatesServiceModel, ProductRatesViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
