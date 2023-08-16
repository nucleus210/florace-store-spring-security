package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.SearchAssembler;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.service.ProfileServiceModel;
import com.nucleus.floracestore.model.service.SearchServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.model.view.ProfileViewModel;
import com.nucleus.floracestore.model.view.SearchViewModel;
import com.nucleus.floracestore.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class SearchController {
    private final ModelMapper modelMapper;
    private final SearchService searchService;
    private final SearchAssembler searchAssembler;

    @Autowired
    public SearchController(ModelMapper modelMapper, SearchService searchService, SearchAssembler searchAssembler) {
        this.modelMapper = modelMapper;
        this.searchService = searchService;
        this.searchAssembler = searchAssembler;
    }

    @GetMapping("/search-results/search/")
    public String searchx(@PathVariable String name) {
        return searchByPage(name, 1);
    }


    private String searchByPage(String name, int i) {

        return name;
    }
    @GetMapping("/search-results/search/{name}")

    public ResponseEntity<CollectionModel<EntityModel<SearchViewModel>>> searchQuery(@PathVariable String name) {
        List<?> searchQuery = searchService.searchQuery(name);
        List<EntityModel<SearchViewModel>> products = searchService.searchQuery(name).stream()
                .map(entity -> searchAssembler.toModel(mapToView((SearchServiceModel) entity))).toList();
       return ResponseEntity.status(HttpStatus.OK)
                .body((CollectionModel<EntityModel<SearchViewModel>>) CollectionModel.of(searchQuery, linkTo(methodOn(SearchController.class).searchQuery(name)).withSelfRel()));
    }
    private SearchViewModel mapToView(SearchServiceModel searchServiceModel) {
        return modelMapper.map(searchServiceModel, SearchViewModel.class);
    }
}
