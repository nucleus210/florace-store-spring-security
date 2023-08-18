package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.*;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.repository.ProductRepository;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              UserService userService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductServiceModel getProductById(Long projectId) {
        ProductEntity productEntity = productRepository.findByProductId(projectId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product " + projectId));
        return mapToService(productEntity);
    }

    @Override
    public ProductServiceModel getByProductName(String productName) {
        ProductEntity productEntity = productRepository.findByProductName(productName)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product " + productName));
        return mapToService(productEntity);
    }

    @Override
    public List<ProductServiceModel> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getAllProductByCategoryName(String categoryName) {
        return productRepository.findAllProductsByProductCategory_ProductCategoryName(categoryName)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getAllProductsByLowerAndUpperPrice(Long lowerPrice, Long upperPrice) {
        return productRepository.findAllProductsByLowerAndUpperPrice(lowerPrice,upperPrice)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel saveProduct(ProductServiceModel productServiceModel, String owner) {
        UserServiceModel userServiceModel = userService.findByUsername(owner);
        productServiceModel.setUser(userServiceModel);
        ProductEntity productEntity = productRepository.save(modelMapper.map(productServiceModel, ProductEntity.class));
        return mapToService(productEntity);
    }

    @Override
    public void updateProduct(ProductServiceModel productServiceModel) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productServiceModel.getProductId());
        productEntity.setProductName(productServiceModel.getProductName());
        productEntity.setUnitQuantity(productServiceModel.getUnitQuantity());
        productEntity.setUnitSellPrice(productServiceModel.getUnitSellPrice());
        productEntity.setUnitOrderPrice(productServiceModel.getUnitOrderPrice());
        productEntity.setUnitDiscount(productServiceModel.getUnitDiscount());
        productEntity.setProductColor(productServiceModel.getProductColor());
        productEntity.setProductSize(productServiceModel.getProductSize());
        productEntity.setProductWeight(productServiceModel.getProductWeight());
        productEntity.setProductDescription(productServiceModel.getProductDescription());
        productEntity.setOtherProductDetails(productServiceModel.getOtherProductDetails());
        productEntity.setProductStatus(modelMapper.map(productServiceModel.getProductStatus(), ProductStatusEntity.class));
        productEntity.setProductCategory(modelMapper.map(productServiceModel.getProductCategory(), ProductCategoryEntity.class));
//        productEntity.setProductSubCategory(modelMapper.map(productServiceModel.getProductSubCategory(), ProductSubCategoryEntity.class));
        productRepository.save(productEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findByProductId(id).ifPresent(productRepository::delete);
    }


    @Override
    public ProductViewModel getByIdAndCurrentUser(Long id, String currentUser) {
        return this.productRepository.
                findById(id).
                map(o -> mapDetailsView(currentUser, o))
                .orElseThrow(()->new QueryRuntimeException("Could not find order item with userId " + id));
    }

    @Override
    public ProductViewModel getById(Long id) {
        return productRepository
                .findByProductId(id)
                .map(this::mapView)
                .orElseThrow(()->new QueryRuntimeException("Could not find order item with productId " + id));
    }

    private ProductViewModel mapDetailsView(String currentUser, ProductEntity product) {
        ProductViewModel productDetailsView = this.modelMapper.map(product, ProductViewModel.class);
        productDetailsView.setCanDelete(isOwner(currentUser, product.getProductId()));
        return productDetailsView;
    }

    private ProductViewModel mapView(ProductEntity product) {
        return this.modelMapper.map(product, ProductViewModel.class);
    }

    private ProductServiceModel mapToService(ProductEntity product) {
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    public boolean isOwner(String userName, Long id) {
        Optional<ProductEntity> productEntity = productRepository.
                findById(id);
        UserServiceModel caller = userService.
                findByUsername(userName);
        if (productEntity.isEmpty() || caller == null) {
            return false;
        } else {
            ProductEntity product = productEntity.get();

            return isAdmin(caller) ||
                    product.getUser().getUsername().equals(userName);
        }
    }

    private boolean isAdmin(UserServiceModel user) {
        return user.
                getRoles().
                stream().
                map(RoleEntity::getRoleName).
                anyMatch(r -> r.equals("ROLE_" + UserRoleEnum.ADMIN));
    }
}
