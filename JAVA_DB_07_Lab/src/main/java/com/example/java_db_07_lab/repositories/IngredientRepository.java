package com.example.java_db_07_lab.repositories;

import com.example.java_db_07_lab.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByNameStartsWith(String pattern);

    List<Ingredient> findAllByNameInOrderByPrice(Collection<String> name);

    @Query("DELETE FROM Ingredient i WHERE i.name = :name")
    @Modifying
    void deleteAllByName(String name);

    @Query("UPDATE Ingredient i SET i.price = i.price * (1+(:percent)/100)")
    @Modifying
    int updateIngredientsPrice(int percent);

    @Query("UPDATE Ingredient i SET i.price = i.price *  (1+(:percent)/100) WHERE i.name IN :ingredients")
    @Modifying
    int updateIngredientsPrice(int percent, List<String> ingredients);
}
