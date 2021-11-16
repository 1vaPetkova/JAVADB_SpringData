package com.example.java_db_09_exercise.controllers;


import com.example.java_db_09_exercise.model.dto.views.Q01.ProductRootNamePriceSellerDto;
import com.example.java_db_09_exercise.model.dto.views.Q02.UserRootSoldProducts;
import com.example.java_db_09_exercise.model.dto.views.Q03.CategoryRootDto;
import com.example.java_db_09_exercise.model.dto.views.Q04.UsersWithSoldProductsRootDto;
import com.example.java_db_09_exercise.services.CategoryService;
import com.example.java_db_09_exercise.services.ProductService;
import com.example.java_db_09_exercise.services.UserService;
import com.example.java_db_09_exercise.util.xmlParser.XMLParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

import static com.example.java_db_09_exercise.util.files.FilePaths.*;

@Component
public class ProductShopController implements CommandLineRunner {


    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader reader;
    private final XMLParser xmlParser;

    public ProductShopController(CategoryService categoryService, UserService userService,
                                 ProductService productService, BufferedReader reader, XMLParser xmlParser) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.reader = reader;
        this.xmlParser = xmlParser;
    }


    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Enter query: ");
        int query = Integer.parseInt(reader.readLine());
        switch (query) {
            case 1 -> Q01ProductsInRange();
            case 2 -> Q02SuccessfullySoldProducts();
            case 3 -> Q03CategoriesByProductsCount();
            case 4 -> Q04UsersAndProducts();
        }

    }


    private void seedData() throws IOException, JAXBException {
        this.categoryService.seedCategories();
        this.userService.seedUsers();
        this.productService.seedProducts();
    }

    private void Q04UsersAndProducts() throws JAXBException {
       UsersWithSoldProductsRootDto result = this.userService
                .findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
        this.xmlParser.writeToFile(Q04_SELLERS_BY_PRODUCTS_COUNT,result);
    }

    private void Q03CategoriesByProductsCount() throws JAXBException {
        CategoryRootDto result = this.categoryService
                .findAllCategoriesOrderByProductsCount();
        this.xmlParser.writeToFile(Q03_CATEGORIES_BY_PRODUCTS, result);
    }

    private void Q02SuccessfullySoldProducts() throws JAXBException {
        UserRootSoldProducts result = this.userService
                .findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName();
        this.xmlParser.writeToFile(Q02_USERS_SOLD_PRODUCTS, result);
    }

    private void Q01ProductsInRange() throws IOException, JAXBException {
        System.out.println("Enter price lower and upper boundary: ");
        BigDecimal lower = new BigDecimal(reader.readLine());
        BigDecimal upper = new BigDecimal(reader.readLine());
        ProductRootNamePriceSellerDto result = this.productService
                .findAllByPriceBetweenAndBuyerIsNull(lower, upper);
        xmlParser.writeToFile(Q01_PRODUCTS_IN_RANGE, result);

    }
}
