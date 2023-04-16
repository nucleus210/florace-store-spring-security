package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.CountryAssembler;
import com.nucleus.floracestore.model.dto.CountryDto;
import com.nucleus.floracestore.model.service.CountryServiceModel;
import com.nucleus.floracestore.model.view.CountryViewModel;
import com.nucleus.floracestore.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class CountryController {

    private final ModelMapper modelMapper;
    private final CountryService countryService;
    private final CountryAssembler assembler;

    @Autowired
    public CountryController(ModelMapper modelMapper, CountryService countryService, CountryAssembler assembler) {
        this.modelMapper = modelMapper;
        this.countryService = countryService;
        this.assembler = assembler;
    }

    @PostMapping("/countries")
    public ResponseEntity<EntityModel<CountryViewModel>> createCountry(@RequestBody CountryDto model) {
        CountryServiceModel countryServiceModel = countryService.createCountry(mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(CountryController.class).createCountry(model)).toUri())
                .body(assembler.toModel(mapToView(countryServiceModel)));
    }

    @PutMapping("/countries/{countryId}")
    public ResponseEntity<EntityModel<CountryViewModel>> updateCountry(@RequestBody CountryDto model, @PathVariable Long countryId) {
        CountryServiceModel countryServiceModel = countryService.updateAnswer(mapToService(model), countryId);
        return ResponseEntity
                .created(linkTo(methodOn(CountryController.class).updateCountry(model, countryId)).toUri())
                .body(assembler.toModel(mapToView(countryServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/countries/{countryId}")
    public ResponseEntity<EntityModel<CountryViewModel>> deleteCountry(@PathVariable Long countryId) {
        EntityModel<CountryViewModel> CountryViewModel = assembler.toModel(mapToView(countryService.deleteCountryById(countryId)));
        return ResponseEntity.status(HttpStatus.OK).body(CountryViewModel);
    }

    @GetMapping("/countries")
    public ResponseEntity<CollectionModel<EntityModel<CountryViewModel>>> getAllCountries() {
        List<EntityModel<CountryViewModel>> questions = countryService.getAllCountries().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(questions, linkTo(methodOn(AnswerController.class).getAllAnswersByUsername()).withSelfRel()));
    }
    @GetMapping("/countries/search/id/{countryId}")
    public ResponseEntity<EntityModel<CountryViewModel>> getCountryById(@PathVariable Long countryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(countryService.getCountryById(countryId))));
    }
    @GetMapping("/answers/search/country-name/{countryName}")
    public ResponseEntity<EntityModel<CountryViewModel>> getCountryByCountryName(@PathVariable String countryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(countryService.getCountryByCountryName(countryName))));
    }
    @GetMapping("/answers/search/country-code/{countryCode}")
    public ResponseEntity<EntityModel<CountryViewModel>> getCountryByCountryCode(@PathVariable String countryCode) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(countryService.getCountryByCountryCode(countryCode))));
    }
    private CountryViewModel mapToView(CountryServiceModel model) {
        return modelMapper.map(model, CountryViewModel.class);
    }

    private CountryServiceModel mapToService(CountryDto model) {
        return modelMapper.map(model, CountryServiceModel.class);
    }

}
