package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CustomerWithPurchasesDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CustomersOrderedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;

import java.io.IOException;
import java.util.List;


public interface CustomerService {
    void seedCustomers() throws IOException;

    Customer findRandomCustomer();

    //Q01
    List<CustomersOrderedDto> findAllByOrderByBirthDateIsYoungDriver();

    //Q05
    List<CustomerWithPurchasesDto> findAllCustomersWithMoreThanOneSale();
}
