package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.repository.ProductCategoryRepository;
import com.nucleus.floracestore.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository,
                                      ModelMapper modelMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductCategoryServiceModel getProductCategoryById(Long productCategoryId) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product category " + productCategoryId));
        return modelMapper.map(productCategoryEntity, ProductCategoryServiceModel.class);
    }

    @Override
    public ProductCategoryServiceModel getProductCategoryByCategoryName(String productCategoryName) {
        return productCategoryRepository.findByProductCategoryName(productCategoryName)
                .map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product category " + productCategoryName));
    }

    @Override
    public List<ProductCategoryServiceModel> getAllProductCategories() {
        return productCategoryRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryServiceModel createProductCategory(ProductCategoryServiceModel category) {
        productCategoryRepository.findByProductCategoryName(category.getProductCategoryName())
                .ifPresentOrElse((value)
                                -> {
                            throw new QueryRuntimeException(String.format("Product category %s already exists",
                                    value.getProductCategoryName()));
                        },
                        () -> productCategoryRepository.save(modelMapper.map(category, ProductCategoryEntity.class)));
        return productCategoryRepository.findByProductCategoryName(category.getProductCategoryName())
                .map(this::mapToService).orElseThrow(
                        () -> new QueryRuntimeException("Could not find product category " + category.getProductCategoryName()));


    }


    @Override
    public ProductCategoryServiceModel updateProductCategoryById(Long productCategoryId,
                                                                 ProductCategoryServiceModel productCategoryServiceModel,
                                                                 String username) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product category with id " + productCategoryId));
        productCategoryEntity.setProductCategoryName(productCategoryServiceModel.getProductCategoryName());
        productCategoryEntity.setProductCategoryDescription(productCategoryServiceModel.getProductCategoryDescription());

        return mapToService(productCategoryRepository.save(productCategoryEntity));
    }

    @Override
    public ProductCategoryServiceModel deleteProductCategoryById(Long productCategoryId) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product category with id " + productCategoryId));
        productCategoryRepository.delete(productCategoryEntity);
        return mapToService(productCategoryEntity);
    }

    @Override
    public void saveAllCategories(Set<ProductCategoryEntity> categories) {
        productCategoryRepository.saveAll(categories);
    }

    private ProductCategoryServiceModel mapToService(ProductCategoryEntity category) {
        return modelMapper.map(category, ProductCategoryServiceModel.class);
    }

    private boolean isAdmin(MyUserPrincipal user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).
                anyMatch(r -> r.equals("ROLE_" + UserRoleEnum.ADMIN));
    }
}
