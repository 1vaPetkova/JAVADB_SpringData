package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.CategoryProductsDto;
import com.example.java_db_09_exercise.model.entities.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();

    //Q03
    List<CategoryProductsDto> findAllCategoriesOrderByProductsCount();
}
