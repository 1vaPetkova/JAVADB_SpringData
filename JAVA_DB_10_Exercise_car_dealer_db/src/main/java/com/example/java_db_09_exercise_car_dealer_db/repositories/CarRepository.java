package com.example.java_db_09_exercise_car_dealer_db.repositories;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    //Q02
    List<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);

    //Q04
    List<Car> findAllBy();
}
