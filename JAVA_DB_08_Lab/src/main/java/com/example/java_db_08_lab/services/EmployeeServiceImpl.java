package com.example.java_db_08_lab.services;

import com.example.java_db_08_lab.model.dto.EmployeeDto;
import com.example.java_db_08_lab.model.dto.ManagerDto;
import com.example.java_db_08_lab.model.entities.Employee;
import com.example.java_db_08_lab.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    //P02
    @Override
    public ManagerDto findManager(Long id) {
        Employee manager = this.employeeRepository.findById(id).orElseThrow();
        return modelMapper.map(manager, ManagerDto.class);
    }

    @Override
    public List<ManagerDto> findAll() {
        return modelMapper.map(this.employeeRepository.findAll(),
                new TypeToken<List<ManagerDto>>() {
                }.getType());
    }

    @Override
    public List<Employee> findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate date) {
        return this.employeeRepository.findAllByBirthdayBeforeOrderBySalaryDesc(date);
    }


    //P01
    @Override
    public EmployeeDto findOne(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow();
        return modelMapper.map(employee, EmployeeDto.class);
    }


//    @Override
//    public EmployeeDto findOne(Long id) {
//        Employee employee = this.employeeRepository.findById(id).orElseThrow();
//        EmployeeDto dto = new EmployeeDto();
//        dto.setFirstName(employee.getFirstName());
//        dto.setLastName(employee.getLastName());
//        dto.setSalary(employee.getSalary());
//        return dto;
//    }
}
