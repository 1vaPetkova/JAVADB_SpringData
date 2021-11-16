package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.ProductNamePriceSellerDto;
import com.example.java_db_09_exercise.model.dto.ProductSeedDto;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.repositories.ProductRepository;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.services.ProductService;
import com.example.java_db_09_exercise.services.UserService;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise.util.files.FilePaths.PRODUCTS_PATH;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileUtil fileUtil;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil,
                              ModelMapper modelMapper, Gson gson,
                              CategoryService categoryService, UserService userService, FileUtil fileUtil) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedProducts() throws IOException {
        if (this.productRepository.count() == 0) {
            ProductSeedDto[] productSeedDtos = gson
                    .fromJson(fileUtil.readFileContent(PRODUCTS_PATH), ProductSeedDto[].class);
            Arrays
                    .stream(productSeedDtos)
                    .filter(validationUtil::isValid)
                    .map(productSeedDto -> {
                                Product product = modelMapper.map(productSeedDto, Product.class);
                                product.setSeller(this.userService.findRandomUser());
                                if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                                    product.setBuyer(this.userService.findRandomUser());
                                }
                                product.setCategories(this.categoryService.findRandomCategories());
                                return product;
                            }
                    )
                    .forEach(this.productRepository::save);
        }
    }

    //Q01
    @Override
    public List<ProductNamePriceSellerDto> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper) {
        return this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream()
                .map(product -> {
                    ProductNamePriceSellerDto productNamePriceSellerDto = modelMapper
                            .map(product, ProductNamePriceSellerDto.class);
                    productNamePriceSellerDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(), product.getSeller().getLastName()));
                    return productNamePriceSellerDto;
                })
                .collect(Collectors.toList());
    }
}
