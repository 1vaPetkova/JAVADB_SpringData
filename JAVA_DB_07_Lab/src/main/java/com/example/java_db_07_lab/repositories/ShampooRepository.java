package com.example.java_db_07_lab.repositories;

import com.example.java_db_07_lab.entities.Shampoo;
import com.example.java_db_07_lab.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {
    //1
    List<Shampoo> findAllBySizeOrderById(Size size);

    //2
    List<Shampoo> findAllBySizeOrLabel_IdOrderByPrice(Size size, Long labelId);

    //3
    List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    //6
    Integer countAllByPriceLessThan(BigDecimal price);

    //7
    @Query("SELECT s FROM Shampoo s JOIN s.ingredients i WHERE i.name IN :ingredients")
    Set<Shampoo> findAllByIngredientsNames(@Param(value = "ingredients") List<String> ingredients);

    //8
    @Query("SELECT s FROM Shampoo s WHERE s.ingredients.size < :count")
    List<Shampoo> findAllByIngredientsCounts(int count);

//    @Query("SELECT s FROM Shampoo s JOIN s.ingredients i GROUP BY s.id HAVING COUNT(i) < :count")
//    List<Shampoo> findAllByIngredientsCounts(long count);


}
