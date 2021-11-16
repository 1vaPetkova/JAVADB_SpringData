package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.ProductNamePriceSellerDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNamePriceSellerDto> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper);
}
