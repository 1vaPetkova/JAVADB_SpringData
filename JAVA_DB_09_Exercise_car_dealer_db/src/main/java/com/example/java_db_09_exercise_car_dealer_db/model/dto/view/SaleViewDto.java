package com.example.java_db_09_exercise_car_dealer_db.model.dto.view;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class SaleViewDto implements Serializable {

    @Expose
    private Integer discount;
    @Expose
    private CarViewDto car;


    public SaleViewDto() {
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public CarViewDto getCar() {
        return car;
    }

    public void setCar(CarViewDto car) {
        this.car = car;
    }
}
