package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarsWithPartsDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    void seedCars() throws IOException;

    Car findRandomCar();

    //Q02
    List<CarViewDto> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);

    //Q04
    List<CarsWithPartsDto> findAllCarsWithParts();
}
