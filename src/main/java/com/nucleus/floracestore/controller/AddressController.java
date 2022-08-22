package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.AddressDto;
import com.nucleus.floracestore.model.enums.AddressTypeEnum;
import com.nucleus.floracestore.model.service.AddressServiceModel;
import com.nucleus.floracestore.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AddressController {

    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressController(AddressService addressService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("addressModel")
    public AddressDto addressModel() {
        return new AddressDto();
    }

    @GetMapping("/address/add")
    public String addressAdd(Model model) {
        if (!model.containsAttribute("addressModel")) {
            model.addAttribute("addressModel", new AddressDto());
        }
        return "address";
    }

    @PostMapping("/address/add")
    public String addressAdd(@Valid AddressDto addressModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addressModel", addressModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addressModel", bindingResult);
            return "redirect:/address/add";
        }

        modelMapper.typeMap(AddressDto.class, AddressServiceModel.class).addMappings(mapper -> {
            mapper.skip(AddressServiceModel::setAddressTypeEnum);
        });
        AddressServiceModel serviceModel = modelMapper.map(addressModel, AddressServiceModel.class);
        serviceModel.setAddressTypeEnum(AddressTypeEnum.valueOf(addressModel.getAddressTypeEnum()));

        addressService.addAddress(serviceModel);
        return "redirect:/home";
    }
}
