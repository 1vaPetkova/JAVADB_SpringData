package com.example.java_db_08_lab.model.dto;

import java.util.Set;

public class ManagerDto extends BasicEmployeeDto {

    private Set<EmployeeDto> subordinates;


    public Set<EmployeeDto> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<EmployeeDto> subordinates) {
        this.subordinates = subordinates;
    }


}


