package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.CategoryProductsDto;
import com.example.java_db_09_exercise.model.dto.CategorySeedDto;
import com.example.java_db_09_exercise.model.entities.Category;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.repositories.CategoryRepository;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise.util.files.FilePaths.CATEGORIES_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }
        CategorySeedDto[] categoriesSeedDto = this.gson
                .fromJson(fileUtil.readFileContent(CATEGORIES_PATH), CategorySeedDto[].class);

        Arrays.stream(categoriesSeedDto)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
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
    public List<CategoryProductsDto> findAllCategoriesOrderByProductsCount() {
        return this.categoryRepository
                .findAllCategoriesOrderByProductsCount()
                .stream()
                .map(category -> {
                    CategoryProductsDto categoryProductsDto = modelMapper
                            .map(category, CategoryProductsDto.class);
                    categoryProductsDto.setCategory(category.getName());
                    categoryProductsDto.setProductsCount(category.getProducts().size());
                    categoryProductsDto.setAveragePrice(getProductsAveragePrice(category.getProducts()));
                    categoryProductsDto.setTotalRevenue(getProductsTotalRevenue(category.getProducts()));
                    return categoryProductsDto;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal getProductsAveragePrice(Set<Product> products) {
        BigDecimal divisor = new BigDecimal(products.size());
        return getProductsTotalRevenue(products).divide(divisor, 6,RoundingMode.UP);
    }

    private BigDecimal getProductsTotalRevenue(Set<Product> products) {
        return products.stream().map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
