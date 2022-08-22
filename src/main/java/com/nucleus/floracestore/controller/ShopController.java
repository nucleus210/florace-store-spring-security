package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final StorageService storageService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public ShopController(ModelMapper modelMapper,
                          UserService userService,
                          StorageService storageService,
                          ProductService productService,
                          ProductCategoryService productCategoryService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.storageService = storageService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }


    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", productCategoryService.getAll());
        return "shop";
    }
}

