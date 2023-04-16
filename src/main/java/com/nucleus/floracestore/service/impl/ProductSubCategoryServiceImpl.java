package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProductSubCategoryEntity;
import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;
import com.nucleus.floracestore.repository.ProductSubCategoryRepository;
import com.nucleus.floracestore.service.ProductSubCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService {

    private final ModelMapper modelMapper;
    private final ProductSubCategoryRepository productSubCategoryRepository;

    @Autowired
    public ProductSubCategoryServiceImpl(ModelMapper modelMapper,
                                         ProductSubCategoryRepository productSubCategoryRepository) {
        this.modelMapper = modelMapper;
        this.productSubCategoryRepository = productSubCategoryRepository;
    }

    @Override
    public ProductSubCategoryServiceModel getProductSubCategoryById(Long productSubCategoryId) {
        ProductSubCategoryEntity productSubCategoryEntity = productSubCategoryRepository
                .findByProductSubCategoryId(productSubCategoryId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product sub category with id " + productSubCategoryId));
        return mapToService(productSubCategoryEntity);
    }

    @Override
    public ProductSubCategoryServiceModel getProductSubCategoryBySubCategoryName(String productSubCategoryName) {
        ProductSubCategoryEntity productSubCategoryEntity = productSubCategoryRepository
                .findByProductSubCategoryName(productSubCategoryName)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product sub category with name " + productSubCategoryName));
        return mapToService(productSubCategoryEntity);
    }

    @Override
    public List<ProductSubCategoryServiceModel> getAllSubCategories() {
        return productSubCategoryRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSubCategoryServiceModel> getAllSubCategoriesByCategoryName(String categoryName) {
        return productSubCategoryRepository
                .findAllSubCategoriesByCategoryName(categoryName)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public ProductSubCategoryServiceModel createProductSubCategory(ProductSubCategoryServiceModel productSubCategoryServiceModel) {
        ProductSubCategoryEntity productSubCategoryEntity =
        productSubCategoryRepository.save(modelMapper.map(productSubCategoryServiceModel, ProductSubCategoryEntity.class));
        return mapToService(productSubCategoryEntity);
    }

    @Override
    public ProductSubCategoryServiceModel updateProductSubCategoryById(Long productSubCategoryId,
                                                                       ProductSubCategoryServiceModel productSubCategoryServiceModel,
                                                                       String username) {
        ProductSubCategoryEntity productSubCategoryEntity =
                productSubCategoryRepository.findByProductSubCategoryId(productSubCategoryId)
                        .orElseThrow(() -> new QueryRuntimeException("Could not find product sub category with id: " + productSubCategoryId));
        productSubCategoryEntity.setProductSubCategoryName(productSubCategoryServiceModel.getProductSubCategoryName());
        productSubCategoryEntity.setProductSubCategoryDescription(productSubCategoryServiceModel.getProductSubCategoryDescription());

        return mapToService(productSubCategoryRepository.save(productSubCategoryEntity));
    }

    @Override
    public ProductSubCategoryServiceModel deleteSubProductCategoryById(Long productSubCategoryId) {
        ProductSubCategoryEntity productSubCategoryEntity =
                productSubCategoryRepository.findByProductSubCategoryId(productSubCategoryId)
                        .orElseThrow(() -> new QueryRuntimeException("Could not find product sub category with id: " + productSubCategoryId));
        productSubCategoryRepository.delete(productSubCategoryEntity);
        return mapToService(productSubCategoryEntity);
    }

    private ProductSubCategoryServiceModel mapToService(ProductSubCategoryEntity productSubCategoryEntity) {
        return modelMapper.map(productSubCategoryEntity, ProductSubCategoryServiceModel.class);
    }
}
