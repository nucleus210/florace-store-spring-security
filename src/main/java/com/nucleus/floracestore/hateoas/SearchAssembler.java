package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.SearchController;
import com.nucleus.floracestore.controller.SliderItemController;
import com.nucleus.floracestore.model.view.SearchViewModel;
import com.nucleus.floracestore.model.view.SliderItemViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class SearchAssembler implements RepresentationModelAssembler<SearchViewModel, EntityModel<SearchViewModel>> {

    private ModelMapper modelMapper;

    @Override
    public EntityModel<SearchViewModel> toModel(SearchViewModel searchViewModel) {

        return EntityModel.of(searchViewModel,
//                linkTo(methodOn(SearchController.class).deleteSearch(searchViewModel.getSearchId())).withRel("deleteSearchItem"),
                linkTo(methodOn(SearchController.class).searchQuery(searchViewModel.getName())).withRel("getAllSliderItems"));
    }
}