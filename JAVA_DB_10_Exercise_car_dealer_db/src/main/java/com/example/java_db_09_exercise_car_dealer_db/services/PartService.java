package com.example.java_db_09_exercise_car_dealer_db.services;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

public interface PartService {

    void seedParts() throws IOException, JAXBException;

    Set<Part> findRandomParts();
}
