package com.example.java_db_09_exercise_car_dealer_db.services;


import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q03.SuppliersLocalRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Supplier;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface SupplierService {

    void seedSuppliers() throws IOException, JAXBException;

    Supplier findRandomSupplier();

    //Q03
    SuppliersLocalRootDto findAllByImporterIsFalse();
}
