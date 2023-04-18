package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.SliderItemController;
import com.nucleus.floracestore.model.dto.SliderItemDto;
import com.nucleus.floracestore.model.view.SliderItemViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SliderItemAssembler implements RepresentationModelAssembler<SliderItemViewModel, EntityModel<SliderItemViewModel>> {

    private ModelMapper modelMapper;

    @Override
    public EntityModel<SliderItemViewModel> toModel(SliderItemViewModel sliderItemViewModel) {

        return EntityModel.of(sliderItemViewModel,
                linkTo(methodOn(SliderItemController.class).deleteSlide(sliderItemViewModel.getSlideItemId())).withRel("deleteSliderItem"),
                linkTo(methodOn(SliderItemController.class).getAllSliderItems()).withRel("getAllSliderItems"));
    }
}