package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.ProductDto;
import com.nucleus.floracestore.model.dto.ProductStorageDto;
import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.service.UserService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public ProductController(ProductService productService,
                             ProductCategoryService productCategoryService,
                             ModelMapper modelMapper, UserService userService, StorageService storageService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.storageService = storageService;
    }

    @ModelAttribute("productModel")
    public ProductDto productModel() {
        return new ProductDto();
    }

    @ModelAttribute("productStorageModel")
    public ProductStorageDto productStorageModel() {
        return new ProductStorageDto();
    }

    @GetMapping("/products/add")
    public String productCreate(Model model) {
        if (!model.containsAttribute("productModel")) {
            model.addAttribute("productModel", productModel());
        }
        model.addAttribute("storages", productStorageModel());
        model.addAttribute("productStatusEnum", ProductStatusEnum.values());
        model.addAttribute("productCategories", productCategoryService.getAll());
        return "product-add-tab";
    }

    @PostMapping("/products/add")
    public String productAdd(@Valid ProductDto productModel,
                             BindingResult bindingResult,
                             ProductStorageDto productStorageModel,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal MyUserPrincipal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("productModel", productStorageModel)
                    .addFlashAttribute("productStorageModel", productStorageModel())
                    .addFlashAttribute("productStatusEnum", ProductStatusEnum.values())
                    .addFlashAttribute("productCategories", productCategoryService.getAll())
                    .addFlashAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return "redirect:/products/add";
        }
        List<StorageEntity> storages = new ArrayList<>();
        String[] storageIds = productStorageModel.getId().split(",");
        for (String id : storageIds) {
            storages.add(storageService.getById(Long.parseLong(id)));
        }

        ProductCategoryEntity productCategory = productCategoryService.getByProductCategoryName(productModel.getCategoryName()).get();

        modelMapper.typeMap(ProductDto.class, ProductServiceModel.class).addMappings(mapper -> {
            mapper.skip(ProductServiceModel::setProductCategory);
        });
        ProductServiceModel serviceModel = modelMapper.map(productModel, ProductServiceModel.class);
        serviceModel.setProductCategory(productCategory);
        serviceModel.setStorageEntity(storages);
        productService.saveProduct(serviceModel, principal.getUserIdentifier());
        return "redirect:/home";
    }

    //    @ResponseBody
    @GetMapping("/products/{id}/edit")
    public String productEdit(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal MyUserPrincipal currentUser) {


        ProductViewModel productViewModel = productService.getByIdAndCurrentUser(id, currentUser.getUsername());
        ProductDto productModel = modelMapper.map(productViewModel, ProductDto.class);
        model.addAttribute("productModel", productModel);
        model.addAttribute("productCategories", productCategoryService.getAll());
        return "product-add-tab";
    }

    @PatchMapping("/products/{id}/edit")
    public String productEdit(@PathVariable Long id,
                              @Valid ProductDto productModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productModel", productModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return "redirect:/edit/{id}";
        }
        ProductServiceModel serviceModel = modelMapper.map(productModel, ProductServiceModel.class);

        productService.updateProduct(serviceModel);
        return "redirect:/details/{id}";
    }

    @GetMapping("/products/{id}/details")
    public String getProductById(@PathVariable Long id, Model model,
                                 @AuthenticationPrincipal MyUserPrincipal myUserPrincipal) {
        model.addAttribute("productDetails", this.productService.getById(id));
        model.addAttribute("relatedProducts", this.productService.getById(id));

        return "shop-details";
    }

    @ResponseBody
    @GetMapping("/products/{productName}/search/")
    public ResponseEntity getProductByName(@PathVariable String productName) {
        Optional<ProductEntity> productOptional = productService.getByProductName(productName);

        if (productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(productOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    @ResponseBody
    @GetMapping(value = "/products/all")
    public ResponseEntity<Object> getAllProducts() {
        List<Object> products = Collections.singletonList(productService.getAllProducts());
        if (!products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/home";
    }

}
