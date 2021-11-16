package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q01.CustomerOrderedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q05.CustomerWithPurchasesRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;

import javax.xml.bind.JAXBException;
import java.io.IOException;


public interface CustomerService {
    void seedCustomers() throws IOException, JAXBException;

    Customer findRandomCustomer();

    //Q01
    CustomerOrderedRootDto findAllByOrderByBirthDateIsYoungDriver();

    //Q05
    CustomerWithPurchasesRootDto findAllCustomersWithMoreThanOneSale();
}
