package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.hateoas.ProductReviewsAssembler;
import com.nucleus.floracestore.model.dto.ProductReviewsDto;
import com.nucleus.floracestore.model.service.ProductReviewsServiceModel;
import com.nucleus.floracestore.model.view.ProductReviewsViewModel;
import com.nucleus.floracestore.service.ProductReviewsService;
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

@Slf4j
@RestController
public class ProductReviewsController {

    private final ModelMapper modelMapper;
    private final ProductReviewsService productReviewsService;
    private final ProductReviewsAssembler assembler;

    @Autowired
    public ProductReviewsController(ModelMapper modelMapper,
                                    ProductReviewsService productReviewsService,
                                    ProductReviewsAssembler assembler) {
        this.modelMapper = modelMapper;
        this.productReviewsService = productReviewsService;
        this.assembler = assembler;
    }

    @PostMapping("/reviews")
    public ProductReviewsServiceModel addProductReview(@RequestBody ProductReviewsDto model) {

        return productReviewsService.writeProductReview(modelMapper.map(model, ProductReviewsServiceModel.class), getCurrentLoggedUsername());
    }

    @PostMapping("/reviews/products/{productId}")
    public ProductReviewsServiceModel writePreview(@RequestBody ProductReviewsDto model, @PathVariable Long productId) {
        return productReviewsService.createProductReview(modelMapper.map(model, ProductReviewsServiceModel.class), productId, getCurrentLoggedUsername());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<EntityModel<ProductReviewsViewModel>> getProductReviewById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(productReviewsService.getProductReviewById(id))));
    }

    @GetMapping("/reviews/products/{productId}/users/{username}")
    public ResponseEntity<EntityModel<ProductReviewsViewModel>> getProductReviewByProductIdAndUsername(@PathVariable Long productId, @PathVariable String username) {
        ProductReviewsServiceModel model = productReviewsService.getProductReviewByProductIdAndUsername(productId, username)
                .orElseThrow(() -> new QueryRuntimeException(
                        String.format("Could not find productReview for productId %s and username %s", productId, username)));

        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(model)));
    }

    @GetMapping(value = "/reviews/search/products/{productId}")
    public ResponseEntity<CollectionModel<EntityModel<ProductReviewsViewModel>>> getAllProductReviewsByProductId(@PathVariable Long productId) {
        List<EntityModel<ProductReviewsViewModel>> productReviews = productReviewsService.getAllProductReviewsByProductId(productId).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productReviews, linkTo(methodOn(ProductReviewsController.class).getAllProductReviewsByProductId(productId)).withSelfRel()));
    }

    private ProductReviewsViewModel mapView(ProductReviewsServiceModel productReviewsServiceModel) {
        modelMapper.typeMap(ProductReviewsServiceModel.class, ProductReviewsViewModel.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getUsername(), ProductReviewsViewModel::setUsername));
        return modelMapper.map(productReviewsServiceModel, ProductReviewsViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}