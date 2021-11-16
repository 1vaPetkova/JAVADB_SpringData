package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.SaleCarCustomerDto;

import java.util.List;

public interface SaleService {
    void seedSales();

    //Q06
    List<SaleCarCustomerDto> findALlSalesWithCustomersAndPrice();
}
