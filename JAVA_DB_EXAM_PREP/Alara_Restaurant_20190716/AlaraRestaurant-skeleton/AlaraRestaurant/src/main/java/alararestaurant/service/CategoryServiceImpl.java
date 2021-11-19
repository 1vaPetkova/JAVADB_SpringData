package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        StringBuilder sb = new StringBuilder();
        List<Category> categories = this.categoryRepository.findAllOrderByCountOfItemsDesc();
        for (Category category : categories) {
            sb
                    .append(String.format("Category: %s", category.getName()))
                    .append(System.lineSeparator());
            category.getItems().forEach(item -> sb
                    .append(String.format("--- Item Name: %s", item.getName()))
                    .append(System.lineSeparator())
                    .append(String.format("--- Item Price: %s", item.getPrice()))
                    .append(System.lineSeparator())
                    .append(System.lineSeparator()));
        }
        return sb.toString();
    }

}
