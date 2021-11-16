package com.example.java_db_06_exercise.service.impl;

import com.example.java_db_06_exercise.model.entity.Category;
import com.example.java_db_06_exercise.repository.CategoryRepository;
import com.example.java_db_06_exercise.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_PATH = "src/main/resources/files/categories.txt";
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(l -> !l.isEmpty())
                .forEach(categoryName -> {
                    Category category = new Category(categoryName);
                    this.categoryRepository.saveAndFlush(category);
                });
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int randomCount = ThreadLocalRandom.current().nextInt(1,5);
        for (int i = 0; i < randomCount; i++) {
            Category category = this.getRandomCategory();
            categories.add(category);
        }
        return categories;
    }

    private Category getRandomCategory() {
        long randomId =  ThreadLocalRandom.current().nextLong(1, this.categoryRepository.count() + 1);
        return this.categoryRepository.findById(randomId).orElse(null);
    }
}
