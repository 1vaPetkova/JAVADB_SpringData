package com.example.java_db_09_exercise.controllers;


import com.example.java_db_09_exercise.model.dto.CategoryProductsDto;
import com.example.java_db_09_exercise.model.dto.ProductNamePriceSellerDto;
import com.example.java_db_09_exercise.model.dto.Q04.SellersByCountDto;
import com.example.java_db_09_exercise.model.dto.UsersSoldProductsDto;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.services.ProductService;
import com.example.java_db_09_exercise.services.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static com.example.java_db_09_exercise.util.files.FilePaths.*;

@Component
public class ProductShopController implements CommandLineRunner {


    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader reader;
    private final Gson gson;

    public ProductShopController(CategoryService categoryService, UserService userService,
                                 ProductService productService, BufferedReader reader, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.reader = reader;
        this.gson = gson;

    }


    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Enter query: ");
        int query = Integer.parseInt(reader.readLine());
        switch (query) {
            case 1 -> Q01ProductsInRange();
            case 2 -> Q02SuccessfullyProducts();
            case 3 -> Q03CategoriesByProductsCount();
            case 4 -> Q04UsersAndProducts();
        }

    }

    private void Q04UsersAndProducts() throws IOException {
        SellersByCountDto sellersByCountDtos = this.userService
                .findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
        String content = gson.toJson(sellersByCountDtos);
        writeToFile(Q04_SELLERS_BY_PRODUCTS_COUNT,content);
    }

    private void Q03CategoriesByProductsCount() throws IOException {
        List<CategoryProductsDto> categoryProductsDtos = this.categoryService
                .findAllCategoriesOrderByProductsCount();
        writeToFile(Q03_CATEGORIES_BY_PRODUCTS, gson.toJson(categoryProductsDtos));
    }

    private void Q02SuccessfullyProducts() throws IOException {
        List<UsersSoldProductsDto> soldProductsDtos = this.userService
                .findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName();
        String content = gson.toJson(soldProductsDtos);
        writeToFile(Q02_USERS_SOLD_PRODUCTS, content);
    }

    private void Q01ProductsInRange() throws IOException {
        System.out.println("Enter price lower and upper boundary: ");
        BigDecimal lower = new BigDecimal(reader.readLine());
        BigDecimal upper = new BigDecimal(reader.readLine());
        List<ProductNamePriceSellerDto> productsDto = productService
                .findAllByPriceBetweenAndBuyerIsNull(lower, upper);
        String content = gson.toJson(productsDto);
        writeToFile(Q01_PRODUCTS_IN_RANGE, content);
    }

    private void writeToFile(String path, String content) throws IOException {
        Files.write(Path.of(path), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        this.categoryService.seedCategories();
        this.userService.seedUsers();
        this.productService.seedProducts();
    }
}
