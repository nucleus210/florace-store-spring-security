package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.ProductCategoryEntity;
import com.nucleus.floracestore.model.view.ProductCategoryViewModel;
import com.nucleus.floracestore.repository.ProductCategoriesRepository;
import com.nucleus.floracestore.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoriesRepository productCategoriesRepository;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoriesRepository productCategoriesRepository) {
        this.productCategoriesRepository = productCategoriesRepository;
    }

    @Override
    public Optional<ProductCategoryEntity> getByProductCategoryName(String productCategoryName) {
        return productCategoriesRepository.findByProductCategoryName(productCategoryName);
    }

    @Override
    public List<ProductCategoryViewModel> getAll() {
        return productCategoriesRepository.findAll().stream()
                .map(productCategory -> {
                    ProductCategoryViewModel productCategoryViewModel =
                            new ProductCategoryViewModel();
                    productCategoryViewModel.setProductCategoryName(productCategory.getProductCategoryName());
                    productCategoryViewModel.setProductCategoryDescription(productCategory.getProductCategoryDescription());
                    return productCategoryViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategoryEntity> getAllEntity() {
        return productCategoriesRepository.findAll();
    }

    @Override
    public ProductCategoryEntity getById(Long id) {
        Optional<ProductCategoryEntity> entity = productCategoriesRepository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        productCategoriesRepository.findAll().forEach(e -> categories.add(e.getProductCategoryName()));
        return categories;
    }

    @Override
    public void saveAll(Set<ProductCategoryEntity> categories) {
        productCategoriesRepository.saveAll(categories);
    }

    @Override
    public void initializeCategories() {
        if (productCategoriesRepository.findAll().isEmpty()) {
            ProductCategoryEntity flowers = new ProductCategoryEntity();
            flowers.setProductCategoryName("Flowers");
            flowers.setProductCategoryDescription("Different types of flowers arranged as bouquets, in a box, in pots.");
            ProductCategoryEntity plants = new ProductCategoryEntity();
            plants.setProductCategoryName("Plants");
            plants.setProductCategoryDescription("Different types of plants for the home and garden.");
            ProductCategoryEntity gifts = new ProductCategoryEntity();
            gifts.setProductCategoryName("Gifts");
            gifts.setProductCategoryDescription("Creative gift solutions made by hand according to customer specific requirements.");
            ProductCategoryEntity special = new ProductCategoryEntity();
            special.setProductCategoryName("Special");
            special.setProductCategoryDescription("Boutique products suitable for special occasions.");
            ProductCategoryEntity cards = new ProductCategoryEntity();
            cards.setProductCategoryName("Cards");
            cards.setProductCategoryDescription("Different types of greeting cards written with special inks and font.");
            ProductCategoryEntity prints = new ProductCategoryEntity();
            prints.setProductCategoryName("Prints");
            prints.setProductCategoryDescription("Printing on various surfaces.");
            ProductCategoryEntity promotions = new ProductCategoryEntity();
            promotions.setProductCategoryName("Promotions");
            promotions.setProductCategoryDescription("Special offers at a super price.");
            productCategoriesRepository.saveAll(Set.of(flowers, plants, gifts, special, cards, prints, promotions));
        }
    }
}
