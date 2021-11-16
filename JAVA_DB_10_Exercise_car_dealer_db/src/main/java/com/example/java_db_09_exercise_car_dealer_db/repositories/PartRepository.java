package com.example.java_db_09_exercise_car_dealer_db.repositories;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

}
