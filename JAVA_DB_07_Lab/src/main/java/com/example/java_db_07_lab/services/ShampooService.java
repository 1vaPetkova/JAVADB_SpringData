package com.example.java_db_07_lab.services;

import com.example.java_db_07_lab.entities.Shampoo;
import com.example.java_db_07_lab.entities.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface ShampooService {
    //1
    List<Shampoo> findAllBySizeOrderById(Size size);

    //2
    List<Shampoo> findAllBySizeOrLabel_IdOrderByPrice(Size size, Long labelId);

    //3
    List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    //6
    Integer countAllByPriceLessThan(BigDecimal price);

    //7
    Set<String> findAllByIngredientsNames(List<String> ingredients);

    //8
    List<String> findAllByIngredientsCount(int count);

}
