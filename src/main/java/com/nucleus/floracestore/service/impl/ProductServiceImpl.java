package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.*;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.repository.ProductRepository;
import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductCategoryService productCategoryService, UserService userService,
                              ModelMapper modelMapper, StorageService storageService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductViewModel getByIdAndCurrentUser(Long id, String currentUser) {
        return this.productRepository.
                findById(id).
                map(o -> mapDetailsView(currentUser, o))
                .get();
    }

    @Override
    public ProductViewModel getById(Long id) {
        return productRepository.findByProductId(id).map(this::mapView).get();
    }

    @Override
    public Optional<ProductEntity> getProductById(Long projectId) {
        return productRepository.findByProductId(projectId);
    }


    @Override
    public Optional<ProductEntity> getByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public ProductServiceModel saveProduct(ProductServiceModel productServiceModel, String owner) {
        UserEntity userEntity = userService.findByUsername(owner).orElseThrow();
        productServiceModel.setOwner(userEntity);
        ProductEntity productEntity = modelMapper.map(productServiceModel, ProductEntity.class);
        productRepository.save(productEntity);
        return productServiceModel;
    }

    @Override
    public List<ProductViewModel> findAllByProductCategory(ProductCategoryEntity productCategory) {
        return this.productRepository.findAllByProductCategory(productCategory)
                .stream().map(this::mapView).collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findByProductId(id).ifPresent(productRepository::delete);
    }

    @Override
    public void updateProduct(ProductServiceModel productServiceModel) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productServiceModel.getProductId());
        productEntity.setProductName(productServiceModel.getProductName());
        productEntity.setProductPrice(productServiceModel.getProductPrice());
        productEntity.setProductColor(productServiceModel.getProductColor());
        productEntity.setProductSize(productServiceModel.getProductSize());
        productEntity.setProductDescription(productServiceModel.getProductDescription());
        productEntity.setOtherProductDetails(productServiceModel.getOtherProductDetails());
        productEntity.setProductStatus(productServiceModel.getProductStatus());
        productEntity.setProductStatus(productServiceModel.getProductStatus());
        productEntity.setProductCategory(productServiceModel.getProductCategory());
        productRepository.save(productEntity);
    }

    private ProductViewModel mapDetailsView(String currentUser, ProductEntity product) {
        ProductViewModel productDetailsView = this.modelMapper.map(product, ProductViewModel.class);
        productDetailsView.setCanDelete(isOwner(currentUser, product.getProductId()));
        return productDetailsView;
    }

    private ProductViewModel mapView(ProductEntity product) {
        return this.modelMapper.map(product, ProductViewModel.class);
    }

    public boolean isOwner(String userName, Long id) {
        Optional<ProductEntity> productEntity = productRepository.
                findById(id);
        Optional<UserEntity> caller = userService.
                findByUsername(userName);
        if (productEntity.isEmpty() || caller.isEmpty()) {
            return false;
        } else {
            ProductEntity product = productEntity.get();

            return isAdmin(caller.get()) ||
                    product.getOwner().equals(userName);
        }
    }

    private boolean isAdmin(UserEntity user) {
        return user.
                getRoles().
                stream().
                map(RoleEntity::getRole).
                anyMatch(r -> r == UserRoleEnum.ADMIN);
    }

    @Override
    public void initializeProducts() {
        if (productRepository.findAll().isEmpty()) {
            List<StorageEntity> storageEntities = storageService.getAllStorageEntities();
            ProductEntity flower01 = new ProductEntity();
            flower01.setProductName("Flower set 01");
            flower01.setProductPrice(new BigDecimal("33.33"));
            flower01.setProductColor("Multy Color");
            flower01.setProductSize("M|L|XL");
            flower01.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower01.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower01.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower01.setProductCategory(productCategoryService.getById(1L));
            flower01.setStorageEntity(List.of(storageEntities.get(1)));
            flower01.setOwner(userService.findByUsername("admin").orElse(null));

            ProductEntity flower02 = new ProductEntity();
            flower02.setProductName("Flower set 02");
            flower02.setProductPrice(new BigDecimal("22.99"));
            flower02.setProductColor("Multy Color");
            flower02.setProductSize("M|L|XL");
            flower02.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower02.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower02.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower02.setProductCategory(productCategoryService.getById(1L));
            flower02.setStorageEntity(List.of(storageEntities.get(2)));
            flower02.setOwner(userService.findByUsername("admin").orElse(null));

            ProductEntity flower03 = new ProductEntity();
            flower03.setProductName("Flower set 03");
            flower03.setProductPrice(new BigDecimal("22.99"));
            flower03.setProductColor("Multy Color");
            flower03.setProductSize("M|L|XL");
            flower03.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower03.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower03.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower03.setProductCategory(productCategoryService.getById(1L));
            flower03.setStorageEntity(List.of(storageEntities.get(3)));
            flower03.setOwner(userService.findByUsername("admin").orElse(null));

            ProductEntity flower04 = new ProductEntity();
            flower04.setProductName("Flower set 04");
            flower04.setProductPrice(new BigDecimal("19.99"));
            flower04.setProductColor("Multy Color");
            flower04.setProductSize("M|L|XL");
            flower04.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower04.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower04.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower04.setProductCategory(productCategoryService.getById(1L));
            flower04.setStorageEntity(List.of(storageEntities.get(4)));
            flower04.setOwner(userService.findByUsername("admin").orElse(null));

            ProductEntity flower05 = new ProductEntity();
            flower05.setProductName("Flower set 05");
            flower05.setProductPrice(new BigDecimal("49.99"));
            flower05.setProductColor("Multy Color");
            flower05.setProductSize("M|L|XL");
            flower05.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower05.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower05.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower05.setProductCategory(productCategoryService.getById(1L));
            flower05.setStorageEntity(List.of(storageEntities.get(5)));
            flower05.setOwner(userService.findByUsername("admin").orElse(null));

            ProductEntity flower06 = new ProductEntity();
            flower06.setProductName("Flower set 06");
            flower06.setProductPrice(new BigDecimal(15.99));
            flower06.setProductColor("Multy Color");
            flower06.setProductSize("M|L|XL");
            flower06.setOtherProductDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum blandit nisi, a faucibus sapien maximus ut.");
            flower06.setProductDescription("Vestibulum suscipit quam et dui scelerisque molestie.Ut non nulla eu ex pretium commodo.In non velit vitae nibh vulputate efficitur vitae id velit.");
            flower06.setProductStatus(ProductStatusEnum.IN_STOCK);
            flower06.setProductCategory(productCategoryService.getById(1L));
            flower06.setStorageEntity(List.of(storageEntities.get(0)));
            flower06.setOwner(userService.findByUsername("admin").orElse(null));
            productRepository.saveAll(Set.of(flower01, flower02, flower03, flower04, flower05, flower06));
        }
    }
}
