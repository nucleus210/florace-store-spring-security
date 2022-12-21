package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProductRateEntity;
import com.nucleus.floracestore.model.service.ProductRatesServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.ProductRatesRepository;
import com.nucleus.floracestore.service.ProductRatesService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductRatesServiceImpl implements ProductRatesService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ProductRatesRepository productRatesRepository;

    @Autowired
    public ProductRatesServiceImpl(UserService userService,
                                   ModelMapper modelMapper,
                                   ProductService productService, ProductRatesRepository productRatesRepository) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.productRatesRepository = productRatesRepository;
    }

    @Override
    public ProductRatesServiceModel getProductRateById(Long productRateId) {
        ProductRateEntity productRatesEntity = productRatesRepository
                .findById(productRateId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find productRate " + productRateId));
        return mapToService(productRatesEntity);
    }

    @Override
    public ProductRatesServiceModel getProductRateByProductIdAndUsername(Long productId, String username) {
        ProductRateEntity productRatesEntity = productRatesRepository.findProductRateByProductIdAndUsername(productId, username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find productRate " + productId));
        return mapToService(productRatesEntity);
    }

    @Override
    public List<ProductRatesServiceModel> getAllProductRatesByProductId(Long productId) {
        return productRatesRepository.findAllProductRatesByProductId(productId).stream()
                .map(this::mapToService).toList();
    }

    @Override
    public ProductRatesServiceModel rateProduct(ProductRatesServiceModel model, Long productId, String username) {
        Optional<ProductRateEntity> productRatesEntity =
                productRatesRepository.findProductRateByProductIdAndUsername(productId, username);
        if (productRatesEntity.isPresent()) {
            throw new QueryRuntimeException("Already rate this product");
        }

        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        model.setUser(userServiceModel);
        ProductRateEntity productRates = productRatesRepository.save(modelMapper.map(model, ProductRateEntity.class));
        return mapToService(productRates);
    }

    private ProductRatesServiceModel mapToService(ProductRateEntity productRates) {
        return this.modelMapper.map(productRates, ProductRatesServiceModel.class);
    }
}

