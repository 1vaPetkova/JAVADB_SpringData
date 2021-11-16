package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.views.Q03.CategoryRootDto;
import com.example.java_db_09_exercise.model.entities.Category;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException, JAXBException;

    Set<Category> findRandomCategories();

    //Q03
    CategoryRootDto findAllCategoriesOrderByProductsCount();
}
