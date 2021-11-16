package com.example.java_db_09_exercise.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class CategorySeedDto {

    @Expose
    private String name;

    public CategorySeedDto() {
    }

    @Size(min = 3,max = 15)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
