package com.example.java_db_07_lab.services;

import com.example.java_db_07_lab.entities.Ingredient;
import com.example.java_db_07_lab.repositories.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    @Override
    public List<Ingredient> findAllByNameStartsWith(String pattern) {
        return this.ingredientRepository.findAllByNameStartsWith(pattern);
    }

    @Override
    public List<Ingredient> findAllByNameInOrderByPrice(Collection<String> names) {
        return this.ingredientRepository.findAllByNameInOrderByPrice(names);
    }

    @Override
    @Transactional
    public void deleteAllByName(String name) {
        this.ingredientRepository.deleteAllByName(name);
    }


    @Override
    @Transactional
    public int updateIngredientsPrice(int percent) {
        return this.ingredientRepository.updateIngredientsPrice(percent);
    }

    @Override
    @Transactional
    public int updateIngredientsPrice(int percent, List<String> ingredients) {
       return this.ingredientRepository.updateIngredientsPrice(percent, ingredients);
    }


}
