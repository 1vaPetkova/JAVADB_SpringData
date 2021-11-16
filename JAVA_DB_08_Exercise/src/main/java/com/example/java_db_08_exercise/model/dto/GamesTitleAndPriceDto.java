package com.example.java_db_08_exercise.model.dto;

import java.math.BigDecimal;

public class GamesTitleAndPriceDto {

    private String title;
    private BigDecimal price;

    public GamesTitleAndPriceDto() {
    }

    public GamesTitleAndPriceDto(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
