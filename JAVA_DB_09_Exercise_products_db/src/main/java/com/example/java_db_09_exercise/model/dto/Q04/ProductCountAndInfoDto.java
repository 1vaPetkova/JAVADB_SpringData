package com.example.java_db_09_exercise.model.dto.Q04;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class ProductCountAndInfoDto {

    //Q04
    @Expose
    private Integer count;
    @Expose
    private Set<ProductNameAndPriceDto> products;

    public ProductCountAndInfoDto() {
        this.products = new HashSet<>();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount() {
        this.count = products.size();
    }

    public Set<ProductNameAndPriceDto> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductNameAndPriceDto> products) {
        this.products = products;
    }
}
