package com.example.java_db_08_lab.services;

import com.example.java_db_08_lab.model.dto.EmployeeCreateRequest;
import com.example.java_db_08_lab.model.dto.EmployeeCreateResponse;
import com.example.java_db_08_lab.model.dto.EmployeeDto;
import com.example.java_db_08_lab.model.dto.ManagerDto;
import com.example.java_db_08_lab.model.entities.Employee;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    EmployeeDto findOne(Long id);

    ManagerDto findManager(Long id);

    List<ManagerDto> findAll();

    List<Employee> findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate date);

    EmployeeCreateResponse save (EmployeeCreateRequest createRequest);
}
