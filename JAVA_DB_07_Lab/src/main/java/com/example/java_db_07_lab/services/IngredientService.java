package com.example.java_db_07_lab.services;

import com.example.java_db_07_lab.entities.Ingredient;

import java.util.Collection;
import java.util.List;

public interface IngredientService {

    //4
    List<Ingredient> findAllByNameStartsWith(String pattern);

    //5
    List<Ingredient> findAllByNameInOrderByPrice(Collection<String> names);

    //9
    void deleteAllByName(String name);

    //10
    int updateIngredientsPrice(int percent);

    //11
    int updateIngredientsPrice(int percent, List<String> ingredients);
}
