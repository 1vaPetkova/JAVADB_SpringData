package com.example.java_db_09_exercise_car_dealer_db.repositories;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findAllByImporterIsFalse();
}
