package com.example.java_db_08_lab.repositories;

import com.example.java_db_08_lab.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate date);
}
