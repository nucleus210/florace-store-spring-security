package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.SliderItemAssembler;
import com.nucleus.floracestore.model.dto.SliderItemDto;
import com.nucleus.floracestore.model.service.SliderItemServiceModel;
import com.nucleus.floracestore.model.view.SliderItemViewModel;
import com.nucleus.floracestore.service.SliderItemService;
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
public class SliderItemController {

    private final ModelMapper modelMapper;
    private final SliderItemService sliderService;
    private final SliderItemAssembler assembler;

    @Autowired
    public SliderItemController(ModelMapper modelMapper,
                                SliderItemService sliderService,
                                SliderItemAssembler assembler) {
        this.modelMapper = modelMapper;
        this.sliderService = sliderService;
        this.assembler = assembler;
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PostMapping("/slider")
    public ResponseEntity<EntityModel<SliderItemViewModel>> createSlide(@RequestBody SliderItemDto model) {
        SliderItemServiceModel sliderItemServiceModel =
                sliderService.createSlide(modelMapper.map(model, SliderItemServiceModel.class), getCurrentLoggedUsername());
        log.info("SliderController: created slide with id: " + sliderItemServiceModel.getSlideItemId());

        return  ResponseEntity
                .created(linkTo(methodOn(SliderItemController.class).createSlide(model)).toUri())
                .body(assembler.toModel(mapToView(sliderItemServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PutMapping("/slider/{id}")
    public ResponseEntity<EntityModel<SliderItemViewModel>> updateSlide(@RequestBody SliderItemDto model) {
        SliderItemServiceModel sliderItemServiceModel = sliderService.updateSlideById(model.getSlideItemId(),
                modelMapper.map(model, SliderItemServiceModel.class),
                getCurrentLoggedUsername());
        log.info("SliderController: update slider item with id: " + sliderItemServiceModel.getSlideItemId());
        return ResponseEntity
                .created(linkTo(methodOn(SliderItemController.class).updateSlide(model)).toUri())
                .body(assembler.toModel(mapToView(sliderItemServiceModel)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/slider/{id}")
    public ResponseEntity<EntityModel<SliderItemViewModel>> deleteSlide(@PathVariable Long sliderItemId) {
        SliderItemServiceModel sliderItemServiceModel = sliderService.deleteSlideById(sliderItemId, getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(SliderItemController.class).deleteSlide(sliderItemId)).toUri())
                .body(assembler.toModel(mapToView(sliderItemServiceModel)));
    }

    public ResponseEntity<CollectionModel<EntityModel<SliderItemViewModel>>> getAllSliderItems() {
        List<EntityModel<SliderItemViewModel>> sliderItems = sliderService.getAllSlides().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(sliderItems, linkTo(methodOn(SliderItemController.class).getAllSliderItems()).withSelfRel()));
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }

    private SliderItemViewModel mapToView(SliderItemServiceModel model) {
        return modelMapper.map(model, SliderItemViewModel.class);
    }
}
