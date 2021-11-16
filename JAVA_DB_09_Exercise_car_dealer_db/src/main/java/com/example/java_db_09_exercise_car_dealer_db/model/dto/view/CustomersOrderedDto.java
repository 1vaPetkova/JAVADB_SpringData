package com.example.java_db_09_exercise_car_dealer_db.model.dto.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CustomersOrderedDto implements Serializable {

    @Expose
    @SerializedName("Id")
    private Long id;

    @Expose
    @SerializedName("Name")
    private String name;

    @Expose
    @SerializedName("BirthDate")
    private String birthDate;

    @Expose
    @SerializedName("IsYoungDriver")
    private Boolean isYoungDriver;

    @Expose
    @SerializedName("Sales")
    private Set<SaleViewDto> sales;

    public CustomersOrderedDto() {
        this.sales = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(Boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public Set<SaleViewDto> getSales() {
        return sales;
    }

    public void setSales(Set<SaleViewDto> sales) {
        this.sales = sales;
    }
}
