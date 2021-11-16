package com.example.java_db_09_exercise_car_dealer_db.repositories;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //Q01
    @Query("SELECT c FROM Customer c ORDER BY c.birthDate, c.birthDate")
    List<Customer> findAllByOrderByBirthDateAscIsYoungDriverAsc();

    //Q05
    @Query("SELECT c FROM Customer c WHERE  c.sales.size > 0 ")
    List<Customer> findAllCustomersWithMoreThanOneSale();
}
