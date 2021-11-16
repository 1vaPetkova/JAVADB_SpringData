package com.example.java_db_06_exercise.repository;

import com.example.java_db_06_exercise.model.enitities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town,Long> {
}
