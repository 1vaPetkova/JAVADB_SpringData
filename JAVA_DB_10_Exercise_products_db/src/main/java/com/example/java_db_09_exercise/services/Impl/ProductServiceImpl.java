package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.seed.ProductSeedRootDto;
import com.example.java_db_09_exercise.model.dto.views.Q01.ProductNamePriceSellerDto;
import com.example.java_db_09_exercise.model.dto.views.Q01.ProductRootNamePriceSellerDto;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.repositories.ProductRepository;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.services.ProductService;
import com.example.java_db_09_exercise.services.UserService;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.example.java_db_09_exercise.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise.util.files.FilePaths.PRODUCTS_PATH;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileUtil fileUtil;
    private final XMLParser xmlParser;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil,
                              ModelMapper modelMapper,
                              CategoryService categoryService, UserService userService, FileUtil fileUtil,
                              XMLParser xmlParser) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public void seedProducts() throws IOException, JAXBException {
        if (this.productRepository.count() == 0) {
            ProductSeedRootDto productsSeedRootDto = this.xmlParser
                    .fromFile(PRODUCTS_PATH, ProductSeedRootDto.class);
            productsSeedRootDto.getProducts()
                    .stream()
                    .filter(this.validationUtil::isValid)
                    .map(productSeedDto -> {
                        Product product = this.modelMapper.map(productSeedDto, Product.class);
                        product.setSeller(this.userService.findRandomUser());
                        if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                            product.setBuyer(this.userService.findRandomUser());
                        }
                        product.setCategories(this.categoryService.findRandomCategories());
                        return product;
                    })
                    .forEach(this.productRepository::save);
        }
    }

    //Q01
    @Override
    public ProductRootNamePriceSellerDto findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper) {
        ProductRootNamePriceSellerDto result = new ProductRootNamePriceSellerDto();
        result.setProducts(this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream()
                .map(product -> {
                    ProductNamePriceSellerDto productNamePriceSellerDto = modelMapper
                            .map(product, ProductNamePriceSellerDto.class);
                    productNamePriceSellerDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName() == null ? "" : product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));
                    return productNamePriceSellerDto;
                })
                .collect(Collectors.toList()));
        return result;
    }
}
