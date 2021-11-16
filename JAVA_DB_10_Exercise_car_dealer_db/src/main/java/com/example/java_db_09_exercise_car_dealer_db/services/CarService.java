package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q02.CarViewRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04.CarsWithPartsRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface CarService {

    void seedCars() throws IOException, JAXBException;

    Car findRandomCar();

    //Q02
    CarViewRootDto findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);

    //Q04
    CarsWithPartsRootDto findAllCarsWithParts();
}
