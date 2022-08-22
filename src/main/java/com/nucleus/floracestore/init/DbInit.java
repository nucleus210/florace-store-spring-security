package com.nucleus.floracestore.init;

import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {

    private final ProductService productService;
    private final UserService userService;
    private final ProductCategoryService productCategoryService;
    private final StorageService storageService;

    public DbInit(ProductService productService,
                  UserService userService,
                  ProductCategoryService productCategoryService,
                  StorageService storageService) {
        this.productService = productService;
        this.userService = userService;
        this.productCategoryService = productCategoryService;
        this.storageService = storageService;
    }


    @Override
    public void run(String... args) throws Exception {
        userService.initializeUsersAndRoles();
        productCategoryService.initializeCategories();
        storageService.initializeStorage();
        productService.initializeProducts();
    }

}