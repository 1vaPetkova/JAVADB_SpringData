package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.seed.CategoriesSeedRootDto;
import com.example.java_db_09_exercise.model.dto.views.Q03.CategoryProductsDto;
import com.example.java_db_09_exercise.model.dto.views.Q03.CategoryRootDto;
import com.example.java_db_09_exercise.model.entities.Category;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.repositories.CategoryRepository;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.util.files.FilePaths;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.example.java_db_09_exercise.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, XMLParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException, JAXBException {
        if (this.categoryRepository.count() > 0) {
            return;
        }
        CategoriesSeedRootDto categoriesSeedRootDto = this.xmlParser
                .fromFile(FilePaths.CATEGORIES_PATH, CategoriesSeedRootDto.class);
        categoriesSeedRootDto.getCategories()
                .stream()
                .filter(this.validationUtil::isValid)
                .map(categorySeedDto -> this.modelMapper.map(categorySeedDto, Category.class))
                .forEach(this.categoryRepository::save);
    }


    @Override
    public Set<Category> findRandomCategories() {
        Set<Category> categories = new HashSet<>();
        long categoriesCount = ThreadLocalRandom
                .current()
                .nextLong(1, 4);
        long count = this.categoryRepository.count();
        for (int i = 0; i < categoriesCount; i++) {
            long randomId = ThreadLocalRandom
                    .current()
                    .nextLong(1, count + 1);
            categories.add(this.categoryRepository.findById(randomId).orElse(null));
        }
        return categories;
    }

    //Q03
    @Override
    public CategoryRootDto findAllCategoriesOrderByProductsCount() {
        CategoryRootDto result = new CategoryRootDto();
        result.setCategories(this.categoryRepository
                .findAllCategoriesOrderByProductsCount()
                .stream()
                .map(category -> {
                    CategoryProductsDto categoryProductsDto = this.modelMapper
                            .map(category, CategoryProductsDto.class);
                    categoryProductsDto.setCategory(category.getName());
                    categoryProductsDto.setProductsCount(category.getProducts().size());
                    categoryProductsDto.setAveragePrice(getProductsAveragePrice(category.getProducts()));
                    categoryProductsDto.setTotalRevenue(getProductsTotalRevenue(category.getProducts()));
                    return categoryProductsDto;
                })
                .collect(Collectors.toList()));
        return result;
    }

    private BigDecimal getProductsAveragePrice(Set<Product> products) {
        BigDecimal divisor = new BigDecimal(products.size());
        return getProductsTotalRevenue(products).divide(divisor, 6, RoundingMode.UP);
    }

    private BigDecimal getProductsTotalRevenue(Set<Product> products) {
        return products.stream().map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
