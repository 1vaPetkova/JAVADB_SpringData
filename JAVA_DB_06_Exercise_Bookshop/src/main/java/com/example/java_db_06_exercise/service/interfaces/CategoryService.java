package com.example.java_db_06_exercise.service.interfaces;


import com.example.java_db_06_exercise.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService  {

    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
