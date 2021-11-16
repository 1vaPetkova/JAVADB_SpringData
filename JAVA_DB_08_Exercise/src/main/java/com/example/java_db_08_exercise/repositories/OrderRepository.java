package com.example.java_db_08_exercise.repositories;

import com.example.java_db_08_exercise.model.entities.Order;
import com.example.java_db_08_exercise.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByBuyer(User buyer);
    Order findByOrderFinalizedFalse();
}
