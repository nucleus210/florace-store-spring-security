package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProductStatusEntity;
import com.nucleus.floracestore.model.service.ProductStatusServiceModel;
import com.nucleus.floracestore.repository.ProductStatusRepository;
import com.nucleus.floracestore.service.ProductStatusService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductStatusServiceImpl implements ProductStatusService {
    private final ModelMapper modelMapper;
    private final ProductStatusRepository productStatusRepository;

    @Autowired
    public ProductStatusServiceImpl(ModelMapper modelMapper, ProductStatusRepository productStatusRepository) {
        this.modelMapper = modelMapper;
        this.productStatusRepository = productStatusRepository;
    }

    @Override
    public ProductStatusServiceModel createProductStatus(ProductStatusServiceModel productStatusServiceModel, String username) {
        ProductStatusEntity productStatusEntity = productStatusRepository.save(modelMapper.map(productStatusServiceModel, ProductStatusEntity.class));
        log.info("ProductStatusService: created product status with id: " + productStatusServiceModel.getProductStatusId());
        return mapToService(productStatusEntity);
    }

    @Override
    public ProductStatusServiceModel updateProductStatusById(Long productStatusId, ProductStatusServiceModel productStatusServiceModel, String username) {
        ProductStatusEntity productStatusEntity = productStatusRepository.findById(productStatusId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product status with id " + productStatusId));
        productStatusEntity.setProductStatusName(productStatusServiceModel.getProductStatusName());
        productStatusEntity.setProductStatusDescription(productStatusServiceModel.getProductStatusDescription());
        productStatusRepository.save(productStatusEntity);

        log.info("ProductStatusService: update product status with id: " + productStatusServiceModel.getProductStatusId());
        return mapToService(productStatusEntity);
    }

    @Override
    public ProductStatusServiceModel deleteProductStatusById(Long productStatusId, String username) {
        ProductStatusEntity productStatusEntity = productStatusRepository.findById(productStatusId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product status with id " + productStatusId));
        productStatusRepository.delete(productStatusEntity);
        log.info("ProductStatusService: deleted product status with id: " + productStatusId);
        return mapToService(productStatusEntity);
    }

    @Override
    public ProductStatusServiceModel getProductStatusById(Long productStatusId) {
        ProductStatusEntity productStatusEntity = productStatusRepository.findById(productStatusId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product status with id " + productStatusId));
        log.info("ProductStatusService: get product status with id: " + productStatusId);
        return mapToService(productStatusEntity);
    }

    @Override
    public ProductStatusServiceModel getProductStatusByProductStatusName(String productStatusName) {
        ProductStatusEntity productStatusEntity = productStatusRepository.findByProductStatusName(productStatusName)
                .orElseThrow(() -> new QueryRuntimeException("Could not find product status with name " + productStatusName));
        log.info("ProductStatusService: get product status with name: " + productStatusName);
        return mapToService(productStatusEntity);
    }

    @Override
    public List<ProductStatusServiceModel> getAllProductStatuses() {
        log.info("ProductStatusService: get all product statuses");

        return productStatusRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }
    private ProductStatusServiceModel mapToService(ProductStatusEntity productStatusEntity) {
        return modelMapper.map(productStatusEntity, ProductStatusServiceModel.class);
    }
}
