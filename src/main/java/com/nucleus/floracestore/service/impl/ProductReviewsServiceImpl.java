package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProductReviewEntity;
import com.nucleus.floracestore.model.service.ProductRatesServiceModel;
import com.nucleus.floracestore.model.service.ProductReviewsServiceModel;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.ProductReviewsRepository;
import com.nucleus.floracestore.service.ProductRatesService;
import com.nucleus.floracestore.service.ProductReviewsService;
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
public class ProductReviewsServiceImpl implements ProductReviewsService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ProductRatesService ratesService;
    private final ProductReviewsRepository productReviewsRepository;

    @Autowired
    public ProductReviewsServiceImpl(UserService userService,
                                     ModelMapper modelMapper,
                                     ProductService productService,
                                     ProductRatesService ratesService, ProductReviewsRepository productReviewsRepository) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.ratesService = ratesService;
        this.productReviewsRepository = productReviewsRepository;
    }


    @Override
    public ProductReviewsServiceModel writeProductReview(ProductReviewsServiceModel model, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));;
                model.setUser(userServiceModel);
        return mapToService(productReviewsRepository.save(modelMapper.map(model, ProductReviewEntity.class)));
    }

    @Override
    public ProductReviewsServiceModel createProductReview(ProductReviewsServiceModel model, Long productId, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        ProductServiceModel product = productService.getProductById(productId);
        ProductRatesServiceModel ratesServiceModel = ratesService.getProductRateByProductIdAndUsername(productId, username);
        model.setRate(ratesServiceModel);
        model.setProduct(product);
        model.setUser(userServiceModel);
        return mapToService(productReviewsRepository.save(modelMapper.map(model, ProductReviewEntity.class)));
    }

    @Override
    public List<ProductReviewsServiceModel> getAllProductReviewsByProductId(Long productId) {
        return productReviewsRepository.findAllProductReviewsByProductId(productId).stream()
                .map(this::mapToService).toList();
    }

    @Override
    public ProductReviewsServiceModel getProductReviewById(Long productReviewId) {
        ProductReviewEntity productReviewEntity = productReviewsRepository
                .findById(productReviewId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find productReview " + productReviewId));
        return mapToService(productReviewEntity);
    }

    @Override
    public Optional<ProductReviewsServiceModel> getProductReviewByProductIdAndUsername(Long productId, String username) {
        ProductReviewEntity productReviewEntity = productReviewsRepository
                .findByProductIdAndUsername(productId, username)
                .orElseThrow(() -> new QueryRuntimeException(
                        String.format("Could not find productReview for productId %s and username %s", productId, username)));
        log.error(productReviewEntity.getTitle());
        return mapToOptionalService(productReviewEntity);
    }

    private ProductReviewsServiceModel mapToService(ProductReviewEntity productReview) {
        return modelMapper.map(productReview, ProductReviewsServiceModel.class);
    }

    private Optional<ProductReviewsServiceModel> mapToOptionalService(ProductReviewEntity productReview) {
        return Optional.of(modelMapper.map(productReview, ProductReviewsServiceModel.class));
    }
}
