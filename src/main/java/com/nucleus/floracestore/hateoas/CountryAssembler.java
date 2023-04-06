package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.CountryController;
import com.nucleus.floracestore.model.view.CountryViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryAssembler implements RepresentationModelAssembler<CountryViewModel, EntityModel<CountryViewModel>> {

    @Override
    public EntityModel<CountryViewModel> toModel(CountryViewModel countryViewModel) {

        return EntityModel.of(countryViewModel,
                linkTo(methodOn(CountryController.class).getCountryById(countryViewModel.getCountryId())).withRel("getCountryById"),
                linkTo(methodOn(CountryController.class).getCountryByCountryName(countryViewModel.getCountryName())).withRel("getCountryByCountryName"),
                linkTo(methodOn(CountryController.class).getCountryByCountryCode(countryViewModel.getCountryCode())).withRel("getCountryByCountryCode"),
                linkTo(methodOn(CountryController.class).deleteCountry(countryViewModel.getCountryId())).withRel("deleteCountryById"),
                linkTo(methodOn(CountryController.class).getAllCountries()).withRel("getAllCountries"));
    }
}