package com.example.java_db_09_exercise_car_dealer_db.services;


import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.SuppliersLocalDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Supplier;

import java.io.IOException;
import java.util.List;

public interface SupplierService {

    void seedSuppliers() throws IOException;

    Supplier findRandomSupplier();

    List<SuppliersLocalDto> findAllByImporterIsFalse();
}
