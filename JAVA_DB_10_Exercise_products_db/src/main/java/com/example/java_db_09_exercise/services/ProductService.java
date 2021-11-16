package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.views.Q01.ProductRootNamePriceSellerDto;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;

public interface ProductService {
    void seedProducts() throws IOException, JAXBException;

    ProductRootNamePriceSellerDto findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper);
}
